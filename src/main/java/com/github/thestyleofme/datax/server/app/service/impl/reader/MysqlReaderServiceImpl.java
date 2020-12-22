package com.github.thestyleofme.datax.server.app.service.impl.reader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.app.service.ReaderService;
import com.github.thestyleofme.datax.server.app.service.impl.PasswordDecoder;
import com.github.thestyleofme.datax.server.app.service.impl.SchemaTableHandler;
import com.github.thestyleofme.datax.server.domain.entity.DataSourceSettingInfo;
import com.github.thestyleofme.datax.server.domain.entity.datax.Job;
import com.github.thestyleofme.datax.server.domain.entity.reader.mysqlreader.MysqlReader;
import com.github.thestyleofme.datax.server.domain.entity.reader.rdbmsreader.ReaderConnection;
import com.github.thestyleofme.driver.core.infra.context.PluginDatasourceHelper;
import com.github.thestyleofme.driver.core.infra.meta.Column;
import com.github.thestyleofme.driver.core.infra.vo.PluginDatasourceVO;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * MySQL Reader
 * </p>
 *
 * @author thestyleofme 2020/12/21 12:49
 * @since 1.0.0
 */
@Service("mysqlReaderServiceImpl")
public class MysqlReaderServiceImpl extends PasswordDecoder implements ReaderService<MysqlReader> {

    private final PluginDatasourceHelper pluginDatasourceHelper;

    public MysqlReaderServiceImpl(PluginDatasourceHelper pluginDatasourceHelper) {
        this.pluginDatasourceHelper = pluginDatasourceHelper;
    }

    @Override
    public String parseReader(Long tenantId, String datasourceCode, String reader) {
        MysqlReader mysqlReader = JsonUtil.toObj(reader, MysqlReader.class);
        PluginDatasourceVO pluginDatasourceVO =
                pluginDatasourceHelper.getDatasourceWithDecryptPwd(tenantId, datasourceCode);
        DataSourceSettingInfo settingInfo = getSettingInfo(pluginDatasourceVO);
        mysqlReader.setPassword(settingInfo.getPassword());
        mysqlReader.setName(null);
        // table改为schema.table的形式
        SchemaTableHandler.handleReader(mysqlReader.getSchema(), mysqlReader.getConnection());
        Job.Reader result = Job.Reader.builder().name("mysqlreader").parameter(mysqlReader).build();
        return JsonUtil.toJson(result);
    }

    @Override
    public String getReaderJson(Long tenantId, String datasourceCode, String schema, String table, List<Column> columnList) {
        List<String> list = columnList.stream().map(Column::getColumnName).collect(Collectors.toList());
        ReaderConnection connection = ReaderConnection.builder().table(Collections.singletonList(table)).build();
        MysqlReader reader = MysqlReader.builder().column(list).connection(Collections.singletonList(connection)).build();
        return JsonUtil.toJson(reader);
    }
}
