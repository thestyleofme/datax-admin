package com.github.thestyleofme.datax.server.app.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.github.thestyleofme.datax.server.domain.entity.writer.BaseWriter;
import com.github.thestyleofme.datax.server.infra.exceptions.DataxException;
import com.github.thestyleofme.driver.core.infra.meta.Column;

/**
 * <p>
 * Writer
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:32
 * @since 1.0.0
 */
public interface WriterService<T extends BaseWriter> {

    /**
     * 解析writer
     *
     * @param tenantId       租户id
     * @param datasourceCode 数据源编码
     * @param writer         json
     * @return json
     */
    String parseWriter(Long tenantId, String datasourceCode, String writer);

    /**
     * 获取WriterService实现类接口的Writer泛型class
     *
     * @return Class
     */
    @SuppressWarnings("unchecked")
    default Class<T> getWriterClass() {
        try {
            return (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        } catch (Exception e) {
            throw new DataxException("Error WriterImpl generic parameter", e);
        }
    }

    /**
     * 获取WriterJson
     *
     * @param tenantId       租户ID
     * @param datasourceCode 数据源编码
     * @param schema         数据库
     * @param table          表名
     * @param columnList     字段列表
     * @return String
     */
    default String getWriterJson(Long tenantId, String datasourceCode, String schema, String table, List<Column> columnList) {
        throw new DataxException("error.datax.datasourceType.unsupported");
    }

    /**
     * 获取字段标准
     *
     * @param writeJson json
     * @return List<Column>
     */
    default List<Column> getStandardColumn(String writeJson) {
        throw new DataxException("error.datax.datasourceType.unsupported");
    }

}
