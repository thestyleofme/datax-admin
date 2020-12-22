package com.github.thestyleofme.datax.server.app.service.impl.writer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.app.service.WriterService;
import com.github.thestyleofme.datax.server.app.service.impl.PasswordDecoder;
import com.github.thestyleofme.datax.server.app.service.impl.SchemaTableHandler;
import com.github.thestyleofme.datax.server.domain.entity.DataSourceSettingInfo;
import com.github.thestyleofme.datax.server.domain.entity.datax.Job;
import com.github.thestyleofme.datax.server.domain.entity.writer.mysqlwriter.MysqlWriter;
import com.github.thestyleofme.datax.server.domain.entity.writer.rdbmswriter.WriterConnection;
import com.github.thestyleofme.driver.core.infra.context.PluginDatasourceHelper;
import com.github.thestyleofme.driver.core.infra.meta.Column;
import com.github.thestyleofme.driver.core.infra.vo.PluginDatasourceVO;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * MySQL Writer
 * </p>
 *
 * @author thestyleofme 2020/12/21 13:59
 * @since 1.0.0
 */
@Service("mysqlWriterServiceImpl")
public class MySqlWriterServiceImpl extends PasswordDecoder implements WriterService<MysqlWriter> {

    private final PluginDatasourceHelper pluginDatasourceHelper;

    public MySqlWriterServiceImpl(PluginDatasourceHelper pluginDatasourceHelper) {
        this.pluginDatasourceHelper = pluginDatasourceHelper;
    }

    @Override
    public String parseWriter(Long tenantId, String datasourceCode, String writer) {
        MysqlWriter mysqlWriter = JsonUtil.toObj(writer, MysqlWriter.class);
        PluginDatasourceVO pluginDatasourceVO =
                pluginDatasourceHelper.getDatasourceWithDecryptPwd(tenantId, datasourceCode);
        DataSourceSettingInfo settingInfo = getSettingInfo(pluginDatasourceVO);
        mysqlWriter.setPassword(settingInfo.getPassword());
        mysqlWriter.setName(null);
        // table改为schema.table的形式
        SchemaTableHandler.handleWriter(mysqlWriter.getSchema(), mysqlWriter.getConnection());
        Job.Writer result = Job.Writer.builder().name("mysqlwriter").parameter(mysqlWriter).build();
        return JsonUtil.toJson(result);
    }


    @Override
    public String getWriterJson(Long tenantId, String datasourceCode, String schema, String table, List<Column> columnList) {
        List<String> list = columnList.stream().map(Column::getColumnName).collect(Collectors.toList());
        WriterConnection connection = WriterConnection.builder().table(Collections.singletonList(table)).build();
        MysqlWriter writer = MysqlWriter.builder().column(list).connection(Collections.singletonList(connection)).build();
        return JsonUtil.toJson(writer);
    }

    @Override
    public List<Column> getStandardColumn(String writeJson) {
        if (StringUtils.isBlank(writeJson)) {
            return Collections.emptyList();
        }
        MysqlWriter writer = JsonUtil.toObj(writeJson, MysqlWriter.class);
        return Optional.ofNullable(writer.getColumn())
                .map(columns -> columns.stream()
                        .map(columnName -> Column.builder().columnName(columnName).build()).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }
}
