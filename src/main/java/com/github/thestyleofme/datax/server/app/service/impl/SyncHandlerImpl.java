package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.github.thestyleofme.datax.server.api.dto.DataxSyncDTO;
import com.github.thestyleofme.datax.server.app.service.ReaderService;
import com.github.thestyleofme.datax.server.app.service.SyncHandler;
import com.github.thestyleofme.datax.server.app.service.WriterService;
import com.github.thestyleofme.datax.server.domain.entity.datax.Job;
import com.github.thestyleofme.datax.server.domain.entity.datax.Param;
import com.github.thestyleofme.datax.server.domain.entity.reader.BaseReader;
import com.github.thestyleofme.datax.server.domain.entity.writer.BaseWriter;
import com.github.thestyleofme.datax.server.infra.exceptions.DataxException;
import com.github.thestyleofme.datax.server.infra.utils.ApplicationContextUtil;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Datax数据同步处理
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:42
 * @since 1.0.0
 */
@Slf4j
@Component
public class SyncHandlerImpl implements SyncHandler {

    @Override
    public String handle(DataxSyncDTO dataxSyncDTO) {
        // 根据reader、writer生成datax的job节点
        Job job = createJob(dataxSyncDTO);
        Param param = Param.builder().job(job).build();
        return StringEscapeUtils.unescapeJava(JsonUtil.toJson(param));
    }

    private Job createJob(DataxSyncDTO dataxSyncDTO) {
        String sourceDatasourceType = dataxSyncDTO.getSourceDatasourceType();
        String writeDatasourceType = dataxSyncDTO.getWriteDatasourceType();
        Map<String, Object> sync = dataxSyncDTO.getSync();
        Object reader = Optional.ofNullable(sync.get("reader"))
                .orElseThrow(() -> new DataxException("error.sync.reader.not.null"));
        Object writer = Optional.ofNullable(sync.get("writer"))
                .orElseThrow(() -> new DataxException("error.sync.writer.not.null"));
        ReaderService<?> readerService = lookupReaderService(sourceDatasourceType);
        String readerJson = JsonUtil.toJson(reader);
        String readJson = readerService.parseReader(dataxSyncDTO.getTenantId(),
                dataxSyncDTO.getSourceDatasourceCode(), readerJson);
        log.debug("readJson: {}", readJson);
        WriterService<?> writerService = lookupWriterService(writeDatasourceType);
        String writerJson = writerService.parseWriter(dataxSyncDTO.getTenantId(),
                dataxSyncDTO.getWriteDatasourceCode(), JsonUtil.toJson(writer));
        log.debug("writerJson: {}", writerJson);
        Job.Content content = Job.Content.builder()
                .reader(JsonUtil.toObj(readJson, Job.Reader.class))
                .writer(JsonUtil.toObj(writerJson, Job.Writer.class))
                .build();
        return Job.builder()
                .setting(parseSetting(sync))
                .content(Collections.singletonList(content))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReaderService<BaseReader> lookupReaderService(String sourceDatasourceType) {
        final String beanName = sourceDatasourceType.toLowerCase() + "ReaderServiceImpl";
        try {
            return ApplicationContextUtil.findBean(beanName, ReaderService.class);
        } catch (BeansException e) {
            log.error("Can not found readerService: {}", beanName);
            throw new DataxException("error.readService.not.found", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public WriterService<BaseWriter> lookupWriterService(String writeDatasourceType) {
        final String beanName = writeDatasourceType.toLowerCase() + "WriterServiceImpl";
        try {
            return ApplicationContextUtil.findBean(beanName, WriterService.class);
        } catch (BeansException e) {
            log.error("Can not found writerService: {}", beanName);
            throw new DataxException("error.readService.not.found", e);
        }
    }

    private Job.Setting parseSetting(Map<String, Object> sync) {
        Object setting = sync.get("setting");
        if (setting == null) {
            throw new DataxException("error.sync.setting.not.null");
        }
        String json = JsonUtil.toJson(setting);
        log.info("setting: {}", json);
        return JsonUtil.toObj(json, Job.Setting.class);
    }

}
