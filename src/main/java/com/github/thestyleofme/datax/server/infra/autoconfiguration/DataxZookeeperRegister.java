package com.github.thestyleofme.datax.server.infra.autoconfiguration;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.github.thestyleofme.datax.server.domain.entity.RegisterDataxInfo;
import com.github.thestyleofme.datax.server.infra.exceptions.DataxException;
import com.github.thestyleofme.plugin.core.infra.utils.JsonUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
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
    private static final String JOB_ID_PATH_PATTERN = "job-id-";
    private static final String JOB_ID_FULL_PATH = "/job-id/" + JOB_ID_PATH_PATTERN;
    private final AtomicReference<String> jobIdPath = new AtomicReference<>(JOB_ID_FULL_PATH);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final InterProcessMutex interProcessMutex;

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
        jobIdPath.set(genJobIdNode(jobIdPath.get() + atomicInteger.get()));
        interProcessMutex = new InterProcessMutex(curatorFramework, "/datax-lock/lock");
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

    /**
     * 基于zk节点版本号生成全局唯一id
     *
     * @return jobId
     */
    public Long genJobId() {
        try {
            interProcessMutex.acquire(3L, TimeUnit.SECONDS);
            long jobId = getJobId();
            if (jobId == Integer.MAX_VALUE) {
                int count = atomicInteger.incrementAndGet();
                genJobIdNode(JOB_ID_FULL_PATH + count);
                jobId = getJobId();
            }
            return jobId + (long) atomicInteger.get() * (Integer.MAX_VALUE - 1);
        } catch (Exception e) {
            throw new DataxException("error generate jobId", e);
        } finally {
            try {
                interProcessMutex.release();
            } catch (Exception e) {
                LOG.error("zk lock release error", e);
            }
        }
    }

    private long getJobId() throws Exception {
        Stat stat = curatorFramework.setData().withVersion(-1).forPath(jobIdPath.get());
        // 最大值Integer.MAX_VALUE=2147483647 故防止不够 一个节点满了下一个 不用担心不够
        return stat.getVersion();
    }

    private String genJobIdNode(String path) {
        try {
            if (null == curatorFramework.checkExists().forPath(path)) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(path);
            }
            // 肯定有值
            Optional<Integer> max = curatorFramework.getChildren().forPath("/job-id")
                    .stream()
                    .map(s -> Integer.parseInt(s.replace(JOB_ID_PATH_PATTERN, "")))
                    .max(Comparator.naturalOrder());
            max.ifPresent(integer -> {
                jobIdPath.set(JOB_ID_FULL_PATH + integer);
                atomicInteger.set(integer);
            });
            return jobIdPath.get();
        } catch (Exception e) {
            throw new DataxException("error create zk node jobId", e);
        }
    }

}
