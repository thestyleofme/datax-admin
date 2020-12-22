package com.github.thestyleofme.datax.server.app.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.github.thestyleofme.datax.server.domain.entity.reader.BaseReader;
import com.github.thestyleofme.datax.server.infra.exceptions.DataxException;
import com.github.thestyleofme.driver.core.infra.meta.Column;

/**
 * <p>
 * Reader
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:32
 * @since 1.0.0
 */
public interface ReaderService<T extends BaseReader> {

    /**
     * 解析reader
     *
     * @param tenantId       租户id
     * @param datasourceCode 数据源编码
     * @param reader         json
     * @return json
     */
    String parseReader(Long tenantId, String datasourceCode, String reader);

    /**
     * 获取ReaderService实现类接口的Reader泛型class
     *
     * @return Class<BaseReader>
     */
    @SuppressWarnings("unchecked")
    default Class<T> getReaderClass() {
        try {
            return (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        } catch (Exception e) {
            throw new DataxException("Error ReaderImpl generic parameter", e);
        }
    }

    /**
     * 按类型实现字段json格式化
     *
     * @param tenantId       租户ID
     * @param datasourceCode 数据源编码
     * @param schema         数据库
     * @param table          表名
     * @param columnList     List<ColumnDTO> 字段
     * @return String json
     */
    default String getReaderJson(Long tenantId, String datasourceCode, String schema, String table, List<Column> columnList) {
        throw new DataxException("error.datax.datasourceType.unsupported");
    }

}
