package com.github.thestyleofme.datax.server.app.service.impl;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.app.service.DataxServerService;
import com.github.thestyleofme.datax.server.app.service.DataxSyncService;
import com.github.thestyleofme.datax.server.domain.entity.DataxJobInfo;
import com.github.thestyleofme.datax.server.domain.entity.DataxSync;
import com.github.thestyleofme.datax.server.domain.entity.RegisterDataxInfo;
import com.github.thestyleofme.datax.server.domain.entity.Result;
import com.github.thestyleofme.datax.server.infra.autoconfiguration.DataxZookeeperRegister;
import com.github.thestyleofme.datax.server.infra.utils.FutureTaskWorker;
import com.github.thestyleofme.datax.server.infra.utils.ThreadPoolUtil;
import com.github.thestyleofme.driver.core.app.service.DriverSessionService;
import com.github.thestyleofme.driver.core.app.service.session.DriverSession;
import com.github.thestyleofme.plugin.core.infra.constants.BaseConstant;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/14 17:14
 * @since 1.0.0
 */
@Service
public class DataxServerServiceImpl implements DataxServerService {

    private static final Logger LOG = LoggerFactory.getLogger(DataxServerServiceImpl.class);
    private static final Long TABLE_SPLIT_NUMBER = 3L;
    private static final String READER_WHERE = "where";
    private static final String READER_QUERY_SQL = "querySql[0]";

    private final DataxZookeeperRegister dataxZookeeperRegister;
    private final RestTemplate restTemplate;
    private final DataxSyncService dataxSyncService;
    private final DriverSessionService driverSessionService;

    public DataxServerServiceImpl(DataxZookeeperRegister dataxZookeeperRegister,
                                  @Qualifier("dataxRibbonRestTemplate") RestTemplate restTemplate,
                                  DataxSyncService dataxSyncService,
                                  DriverSessionService driverSessionService) {
        this.dataxZookeeperRegister = dataxZookeeperRegister;
        this.restTemplate = restTemplate;
        this.dataxSyncService = dataxSyncService;
        this.driverSessionService = driverSessionService;
    }

    @Override
    public String index() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://DATAX/index", String.class);
        return forEntity.getBody();
    }

    @Override
    public List<RegisterDataxInfo> getAllDataxNode() {
        return dataxZookeeperRegister.getAllDataxNode();
    }

    private Result<String> doDataxJob(DataxJobInfo dataxJobInfo) {
        HttpEntity<String> requestEntity =
                new HttpEntity<>(JsonUtil.toJson(dataxJobInfo), applicationJsonHeaders());
        return restTemplate.exchange("http://DATAX/datax/job", HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<Result<String>>() {
                }).getBody();
    }

    private List<Result<String>> handleDataxJob(DataxJobInfo dataxJobInfo) {
        // 不是所有的reader都能分片，目前先支持比如mysql的jdbc类型数据库 写table或querySql方式
        List<DataxJobInfo> dataxJobInfoList = doReaderSplit(dataxJobInfo);
        FutureTaskWorker<DataxJobInfo, Result<String>> futureTaskWorker = new FutureTaskWorker<>(dataxJobInfoList,
                o -> CompletableFuture.supplyAsync(() -> doDataxJob(o),
                        ThreadPoolUtil.getExecutorService()));
        return futureTaskWorker.getFutureList().stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return Result.fail("execute datax job error: InterruptedException", getMessage(e));
                    } catch (ExecutionException e) {
                        LOG.error("execute datax job error: ExecutionException", e);
                        return Result.fail("execute datax job error", getMessage(e));
                    }
                })
                .collect(Collectors.toList());
    }

    private List<DataxJobInfo> doReaderSplit(DataxJobInfo dataxJobInfo) {
        // 只有传了syncId和分片字段才做reader分片处理，因为负载的原因，传job可能负载的机器没有该文件
        if (StringUtils.isEmpty(dataxJobInfo.getSplitCol()) || dataxJobInfo.getSyncId() == null) {
            return Collections.singletonList(dataxJobInfo);
        }
        // TODO syncId
        ONode rootNode = ONode.loadStr(StringUtils.isEmpty(dataxJobInfo.getJobJson()) ? "" : dataxJobInfo.getJobJson());
        String table = rootNode.select("$.job.content[0].reader.parameter.connection[0].table[0]").getString();
        if (!StringUtils.isEmpty(table)) {
            // table方式不为空 json中增加where，并生成多个json，提交到集群
            // 按时间或主键字段切割
            return readerSplitByTable(table, dataxJobInfo, rootNode);
        }
        String querySql = rootNode.select("$.job.content[0].reader.parameter.connection[0].querySql[0]").getString();
        if (!StringUtils.isEmpty(querySql)) {
            // querySql方式不为空 json中修改此querySql，并生成多个json，提交到集群
            // 按时间或主键字段切割
            return readerSplitByQuerySql(querySql, dataxJobInfo, rootNode);
        }
        // 不支持分片
        return Collections.singletonList(dataxJobInfo);
    }

    private List<DataxJobInfo> readerSplitByTable(String table, DataxJobInfo dataxJobInfo, ONode rootNode) {
        DataxSync dataxSync = dataxSyncService.getById(dataxJobInfo.getSyncId());
        DriverSession driverSession = driverSessionService.getDriverSession(dataxSync.getTenantId(), dataxSync.getSourceDatasourceCode());
        // 超过一定数据量才分片
        Long sourceCount = driverSession.queryCount(dataxSync.getSourceSchema(), "select 1 from " + table);
        if (sourceCount > TABLE_SPLIT_NUMBER) {
            // 分批
            String splitCol = dataxJobInfo.getSplitCol();
            if (DataxJobInfo.PK_SPLIT_TYPE.equals(dataxJobInfo.getSplitType())) {
                return splitByPkUseWhere(dataxJobInfo, rootNode, table, dataxSync, driverSession, splitCol);
            }
            return splitByDateUseWhere(dataxJobInfo, rootNode, table, dataxSync, driverSession, splitCol);
        }
        return Collections.singletonList(dataxJobInfo);
    }

    private List<DataxJobInfo> splitByDateUseWhere(DataxJobInfo dataxJobInfo,
                                                   ONode rootNode,
                                                   String table,
                                                   DataxSync dataxSync,
                                                   DriverSession driverSession,
                                                   String splitCol) {
        String where = rootNode.select("$.job.content[0].reader.parameter.where").getString();
        List<Map<String, Object>> list = queryMinAndMax(table, dataxSync, driverSession, splitCol, where);
        // 只有一个
        LocalDateTime min = ((Timestamp) list.get(0).get("min")).toLocalDateTime();
        LocalDateTime max = ((Timestamp) list.get(0).get("max")).toLocalDateTime();
        int splitNumber = dataxJobInfo.getSplitNumber();
        long size = Duration.between(min, max).dividedBy(splitNumber).toMinutes();
        List<DataxJobInfo> resultList = new ArrayList<>(splitNumber + 1);
        ONode parameterNode = rootNode.select("$.job.content[0].reader.parameter");
        String preWhere = StringUtils.isEmpty(where) ? "" : where + " and";
        String start = driverSession.toDate(min.format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null);
        String end = driverSession.toDate(min.plusMinutes(size).format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null);
        for (int i = 1; i <= splitNumber; i++) {
            if (i == splitNumber) {
                // 分批中的最后一个
                parameterNode.set(READER_WHERE, String.format("%s (%s <= %s AND %s <= %s)", preWhere, start, splitCol, splitCol,
                        driverSession.toDate(max.format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null)));
            } else {
                parameterNode.set(READER_WHERE, String.format("%s (%s <= %s AND %s < %s)", preWhere, start, splitCol, splitCol, end));
            }
            add(dataxJobInfo, rootNode, resultList);
            if (i != splitNumber) {
                start = end;
                end = driverSession.toDate(min.plusMinutes(size * (i + 1)).format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null);
            }
        }
        // 防止漏了 再加一个为空的
        parameterNode.set(READER_WHERE, String.format("%s %s IS NULL", preWhere, splitCol));
        add(dataxJobInfo, rootNode, resultList);
        return resultList;
    }

    private List<DataxJobInfo> splitByPkUseWhere(DataxJobInfo dataxJobInfo,
                                                 ONode rootNode,
                                                 String table,
                                                 DataxSync dataxSync,
                                                 DriverSession driverSession,
                                                 String splitCol) {
        String where = rootNode.select("$.job.content[0].reader.parameter.where").getString();
        List<Map<String, Object>> list = queryMinAndMax(table, dataxSync, driverSession, splitCol, where);
        // 只有一个
        Long min = (Long) list.get(0).get("min");
        Long max = (Long) list.get(0).get("max");
        int splitNumber = dataxJobInfo.getSplitNumber();
        long size = (min + max) / splitNumber;
        List<DataxJobInfo> resultList = new ArrayList<>(splitNumber + 1);
        ONode parameterNode = rootNode.select("$.job.content[0].reader.parameter");
        long start = min;
        long end = size;
        String preWhere = StringUtils.isEmpty(where) ? "" : where + " and";
        for (int i = 1; i <= splitNumber; i++) {
            if (i == splitNumber) {
                // 分批中的最后一个
                parameterNode.set(READER_WHERE, String.format("%s (%d <= %s AND %s <= %d)", preWhere, start, splitCol, splitCol, max));
            } else {
                parameterNode.set(READER_WHERE, String.format("%s (%d <= %s AND %s < %d)", preWhere, start, splitCol, splitCol, end));
            }
            add(dataxJobInfo, rootNode, resultList);
            if (i != splitNumber) {
                start = end;
                end *= (i + 1);
            }
        }
        // 防止漏了 再加一个为空的
        parameterNode.set(READER_WHERE, String.format("%s %s IS NULL", preWhere, splitCol));
        add(dataxJobInfo, rootNode, resultList);
        return resultList;
    }

    private List<Map<String, Object>> queryMinAndMax(String table,
                                                     DataxSync dataxSync,
                                                     DriverSession driverSession,
                                                     String splitCol,
                                                     String where) {
        return driverSession.executeOneQuery(dataxSync.getSourceSchema(),
                String.format("SELECT MIN(%s) min,MAX(%s) max FROM %s %s",
                        splitCol, splitCol, table, StringUtils.isEmpty(where) ? "" : "where " + where));
    }

    private List<DataxJobInfo> readerSplitByQuerySql(String querySql, DataxJobInfo dataxJobInfo, ONode rootNode) {
        DataxSync dataxSync = dataxSyncService.getById(dataxJobInfo.getSyncId());
        DriverSession driverSession = driverSessionService.getDriverSession(dataxSync.getTenantId(), dataxSync.getSourceDatasourceCode());
        // 超过一定数据量才分片
        Long sourceCount = driverSession.queryCount(dataxSync.getSourceSchema(), querySql);
        if (sourceCount > TABLE_SPLIT_NUMBER) {
            // 分批
            String splitCol = dataxJobInfo.getSplitCol();
            if (DataxJobInfo.PK_SPLIT_TYPE.equals(dataxJobInfo.getSplitType())) {
                return splitByPkUseSql(driverSession, dataxJobInfo, rootNode, querySql, splitCol);
            }
            return splitByDateUseSql(driverSession, dataxJobInfo, rootNode, querySql, splitCol);
        }
        return Collections.singletonList(dataxJobInfo);
    }

    private List<DataxJobInfo> splitByDateUseSql(DriverSession driverSession,
                                                 DataxJobInfo dataxJobInfo,
                                                 ONode rootNode,
                                                 String querySql,
                                                 String splitCol) {
        List<Map<String, Object>> list = driverSession.executeOneQuery(null, String.format(
                "SELECT MIN(_t.%s) min,MAX(_t.%s) max from (%s) _t", splitCol, splitCol, querySql));
        LocalDateTime max = ((Timestamp) list.get(0).get("max")).toLocalDateTime();
        LocalDateTime min = ((Timestamp) list.get(0).get("min")).toLocalDateTime();
        int splitNumber = dataxJobInfo.getSplitNumber();
        long size = Duration.between(min, max).dividedBy(splitNumber).toMinutes();
        List<DataxJobInfo> resultList = new ArrayList<>(splitNumber + 1);
        ONode connectionNode = rootNode.select("$.job.content[0].reader.parameter.connection[0]");
        String start = driverSession.toDate(min.format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null);
        String end = driverSession.toDate(min.plusMinutes(size).format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null);
        for (int i = 1; i <= splitNumber; i++) {
            if (i == splitNumber) {
                // 分批中的最后一个
                connectionNode.set(READER_QUERY_SQL, String.format("select _t.* from (%s) _t where (%s <= _t.%s AND _t.%s <= %s)", querySql, start, splitCol, splitCol,
                        driverSession.toDate(max.format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null)));
            } else {
                connectionNode.set(READER_QUERY_SQL, String.format("select _t.* from (%s) _t where (%s <= _t.%s AND _t.%s < %s)", querySql, start, splitCol, splitCol, end));
            }
            add(dataxJobInfo, rootNode, resultList);
            if (i != splitNumber) {
                start = end;
                end = driverSession.toDate(min.plusMinutes(size * (i + 1)).format(DateTimeFormatter.ofPattern(BaseConstant.Pattern.DATETIME)), null);
            }
        }
        // 防止漏了 再加一个为空的
        connectionNode.set(READER_QUERY_SQL, String.format("select _t.* from (%s) _t where _t.%s IS NULL", querySql, splitCol));
        add(dataxJobInfo, rootNode, resultList);
        return resultList;
    }

    private void add(DataxJobInfo dataxJobInfo, ONode rootNode, List<DataxJobInfo> resultList) {
        dataxJobInfo.setJobJson(rootNode.toJson());
        resultList.add(DataxJobInfo.build(dataxJobInfo));
    }

    private List<DataxJobInfo> splitByPkUseSql(DriverSession driverSession,
                                               DataxJobInfo dataxJobInfo,
                                               ONode rootNode,
                                               String querySql,
                                               String splitCol) {
        // querySql方式必须查询主键
        List<Map<String, Object>> list = driverSession.executeOneQuery(null, String.format(
                "SELECT MIN(_t.%s) min,MAX(_t.%s) max from (%s) _t", splitCol, splitCol, querySql));
        Long min = Long.valueOf(String.valueOf(list.get(0).get("min")));
        Long max = Long.valueOf(String.valueOf(list.get(0).get("max")));
        int splitNumber = dataxJobInfo.getSplitNumber();
        long size = (min + max) / splitNumber;
        List<DataxJobInfo> resultList = new ArrayList<>(splitNumber + 1);
        ONode connectionNode = rootNode.select("$.job.content[0].reader.parameter.connection[0]");
        long start = min;
        long end = size;
        for (int i = 1; i <= splitNumber; i++) {
            if (i == splitNumber) {
                // 分批中的最后一个
                connectionNode.set(READER_QUERY_SQL, String.format("select _t.* from (%s) _t where (%d <= _t.%s AND _t.%s <= %d)", querySql, start, splitCol, splitCol, max));
            } else {
                connectionNode.set(READER_QUERY_SQL, String.format("select _t.* from (%s) _t where (%d <= _t.%s AND _t.%s < %d)", querySql, start, splitCol, splitCol, end));
            }
            add(dataxJobInfo, rootNode, resultList);
            if (i != splitNumber) {
                start = end;
                end *= (i + 1);
            }
        }
        // 防止漏了 再加一个为空的
        connectionNode.set(READER_QUERY_SQL, String.format("select _t.* from (%s) _t where _t.%s IS NULL", querySql, splitCol));
        add(dataxJobInfo, rootNode, resultList);
        return resultList;
    }

    @Override
    public List<Result<String>> executeDataxJobList(List<DataxJobInfo> dataxJobInfoList) {
        LOG.debug("starting execute datax job...");
        return dataxJobInfoList.stream()
                .map(this::handleDataxJob)
                .flatMap(o -> new ArrayList<>(o).stream())
                .collect(Collectors.toList());
    }

    private HttpHeaders applicationJsonHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    /**
     * 获取原始的错误信息，如果没有cause则返回当前message
     *
     * @param e Exception
     * @return 错误信息
     */
    public static String getMessage(Exception e) {
        Throwable cause = e.getCause();
        if (cause == null) {
            return e.getMessage();
        }
        return cause.getMessage();
    }
}
