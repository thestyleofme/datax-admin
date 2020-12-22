package com.github.thestyleofme.datax.server.infra.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/14 16:03
 * @since 1.0.0
 */
@EnableConfigurationProperties(DataxServerZookeeperInfo.class)
@ConfigurationProperties(prefix = "datax.zookeeper")
public class DataxServerZookeeperInfo {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
