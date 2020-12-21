package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.app.service.DataxServerService;
import com.github.thestyleofme.datax.server.domain.entity.DataxJobInfo;
import com.github.thestyleofme.datax.server.domain.entity.RegisterDataxInfo;
import com.github.thestyleofme.datax.server.domain.entity.Result;
import com.github.thestyleofme.datax.server.infra.autoconfiguration.DataxZookeeperRegister;
import com.github.thestyleofme.datax.server.infra.utils.FutureTaskWorker;
import com.github.thestyleofme.datax.server.infra.utils.JsonUtil;
import com.github.thestyleofme.datax.server.infra.utils.ThreadPoolUtil;
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
 * @author isaac 2020/12/14 17:14
 * @since 1.0.0
 */
@Service
public class DataxServerServiceImpl implements DataxServerService {

    private static final Logger LOG = LoggerFactory.getLogger(DataxServerServiceImpl.class);

    private final DataxZookeeperRegister dataxZookeeperRegister;
    private final RestTemplate restTemplate;

    public DataxServerServiceImpl(DataxZookeeperRegister dataxZookeeperRegister,
                                  @Qualifier("dataxRibbonRestTemplate") RestTemplate restTemplate) {
        this.dataxZookeeperRegister = dataxZookeeperRegister;
        this.restTemplate = restTemplate;
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
        ONode rootNode = ONode.loadStr(dataxJobInfo.getJobJson());
        String table = rootNode.select("$.job.content[0].reader.parameter.connection[0].table[0]").getString();
        if (!StringUtils.isEmpty(table)) {
            // table方式不为空 json中增加where，并生成多个json，提交到集群
            // 按时间或主键字段切割
            return readerSplitByTable(table,dataxJobInfo);
        }
        String querySql = rootNode.select("$.job.content[0].reader.parameter.connection[0].querySql").getString();
        if (!StringUtils.isEmpty(querySql)) {
            // querySql方式不为空 json中修改此querySql，并生成多个json，提交到集群
            // 按时间或主键字段切割
            return readerSplitByQuerySql(querySql,dataxJobInfo);
        }
        // 不支持分片
        return Collections.singletonList(dataxJobInfo);
    }

    private List<DataxJobInfo> readerSplitByTable(String table, DataxJobInfo dataxJobInfo) {
        return Collections.singletonList(dataxJobInfo);
    }

    private List<DataxJobInfo> readerSplitByQuerySql(String querySql, DataxJobInfo dataxJobInfo) {
        return Collections.singletonList(dataxJobInfo);
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
