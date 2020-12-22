package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.domain.entity.reader.rdbmsreader.ReaderConnection;
import com.github.thestyleofme.datax.server.domain.entity.writer.rdbmswriter.WriterConnection;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * schema与table处理类
 * </p>
 *
 * @author thestyleofme 2020/12/21 13:56
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class SchemaTableHandler {

    /**
     * 将readerConnection中的table改为schema.table的格式
     *
     * @param schema      数据库
     * @param connections 连接
     */
    public static void handleReader(String schema, List<ReaderConnection> connections) {
        if (StringUtils.isNotBlank(schema) && CollectionUtils.isNotEmpty(connections)) {
            connections.forEach(connection -> {
                if (CollectionUtils.isNotEmpty(connection.getTable())) {
                    connection.setTable(connectSchemaTable(schema, connection.getTable()));
                }
            });
        }
    }

    /**
     * 将writerConnection中的table改为schema.table的格式
     *
     * @param schema      数据库
     * @param connections 连接
     */
    public static void handleWriter(String schema, List<WriterConnection> connections) {
        if (StringUtils.isNotBlank(schema) && CollectionUtils.isNotEmpty(connections)) {
            connections.forEach(connection -> {
                if (CollectionUtils.isNotEmpty(connection.getTable())) {
                    connection.setTable(connectSchemaTable(schema, connection.getTable()));
                }
            });
        }
    }

    /**
     * 拼接
     *
     * @param schema 数据库
     * @param tables 表
     * @return 表
     */
    private static List<String> connectSchemaTable(String schema, List<String> tables) {
        return tables.stream().map(table -> String.format("%s.%s", schema, table)).collect(Collectors.toList());
    }

}
