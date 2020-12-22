package com.github.thestyleofme.datax.server.infra.autoconfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.domain.entity.RegisterDataxInfo;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/14 16:25
 * @since 1.0.0
 */
public class DataxZookeeperRegister {

    private static final Logger LOG = LoggerFactory.getLogger(DataxZookeeperRegister.class);

    private final CuratorFramework curatorFramework;

    public DataxZookeeperRegister(DataxServerZookeeperInfo dataxServerZookeeperInfo) {
        RetryPolicy backoffRetry = new ExponentialBackoffRetry(3000, 2);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(dataxServerZookeeperInfo.getAddress())
                .sessionTimeoutMs(30000)
                .connectionTimeoutMs(30000)
                .retryPolicy(backoffRetry)
                // namespace跟datax server保持一致
                .namespace("datax")
                .build();
        curatorFramework.start();
        LOG.debug("connect to zookeeper register...");
    }

    public List<RegisterDataxInfo> getAllDataxNode() {
        try {
            // path跟datax server保持一致
            List<String> list = curatorFramework.getChildren().forPath("/cluster");
            return list.stream()
                    .map(s -> {
                        try {
                            byte[] bytes = curatorFramework.getData().forPath("/cluster" + "/" + s);
                            return JsonUtil.toObj(new String(bytes, StandardCharsets.UTF_8), RegisterDataxInfo.class);
                        } catch (Exception e) {
                            LOG.error("get datax server node info error", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("failed get datax server node list", e);
            return Collections.emptyList();
        }
    }

}
