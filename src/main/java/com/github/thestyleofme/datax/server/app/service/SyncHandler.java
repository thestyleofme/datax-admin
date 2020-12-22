package com.github.thestyleofme.datax.server.app.service;


import com.github.thestyleofme.datax.server.api.dto.DataxSyncDTO;
import com.github.thestyleofme.datax.server.domain.entity.reader.BaseReader;
import com.github.thestyleofme.datax.server.domain.entity.writer.BaseWriter;

/**
 * <p>
 * Datax数据同步处理类
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:27
 * @since 1.0.0
 */
public interface SyncHandler {

    /**
     * Datax数据同步处理方法
     *
     * @param dataxSyncDTO DataxSyncDTO
     * @return datax json字符串
     */
    String handle(DataxSyncDTO dataxSyncDTO);

    /**
     * 获取ReaderService实现类
     *
     * @param sourceDatasourceType 来源数据源类型
     * @return ReaderService
     */
    ReaderService<BaseReader> lookupReaderService(String sourceDatasourceType);

    /**
     * 获取WriterService实现类
     *
     * @param writeDatasourceType 写入数据源类型
     * @return WriterService
     */
    WriterService<BaseWriter> lookupWriterService(String writeDatasourceType);
}
