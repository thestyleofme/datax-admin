package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.Properties;

import com.github.thestyleofme.datax.server.domain.entity.DataSourceSettingInfo;
import com.github.thestyleofme.driver.core.infra.utils.DriverUtil;
import com.github.thestyleofme.driver.core.infra.vo.PluginDatasourceVO;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
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

    private static final String PASSWORD = "password";
    private static final String ACCESS_KEY = "accessKey";

    protected DataSourceSettingInfo getSettingInfo(PluginDatasourceVO pluginDatasourceVO) {
        Properties properties = DriverUtil.parseDatasourceSettingInfo(pluginDatasourceVO);
        return JsonUtil.toObj(JsonUtil.toJson(properties), DataSourceSettingInfo.class);
    }

    /**
     * 隐藏密码，设置密码为******
     *
     * @param json datax json
     * @return json
     */
    public static String hidePassword(String json) {
        ONode rootNode = ONode.loadStr(json);
        rootNode.select("$..parameter[?(@.password)]").forEach(oNode -> oNode.set(PASSWORD, "******"));
        rootNode.select("$..parameter[?(@.accessKey)]").forEach(oNode -> oNode.set(ACCESS_KEY, "******"));
        return rootNode.toJson();
    }

}
