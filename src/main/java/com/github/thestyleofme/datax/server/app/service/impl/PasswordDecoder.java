package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.Properties;

import com.github.thestyleofme.datax.server.domain.entity.DataSourceSettingInfo;
import com.github.thestyleofme.driver.core.infra.utils.DriverUtil;
import com.github.thestyleofme.driver.core.infra.vo.PluginDatasourceVO;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 解密密码
 * </p>
 *
 * @author thestyleofme 2020/12/21 12:55
 * @since 1.0.0
 */
@Slf4j
@Service
public class PasswordDecoder {

    protected DataSourceSettingInfo getSettingInfo(PluginDatasourceVO pluginDatasourceVO) {
        Properties properties = DriverUtil.parseDatasourceSettingInfo(pluginDatasourceVO);
        return JsonUtil.toObj(JsonUtil.toJson(properties), DataSourceSettingInfo.class);
    }

}
