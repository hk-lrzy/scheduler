package com.hklrzy.scheduler.common.zkclient;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.hklrzy.scheduler.common.exception.ErrorCode;
import com.hklrzy.scheduler.common.exception.SchedulerException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class ZkClient implements Closeable {

    private static final Logger LOG =
            LoggerFactory.getLogger(ZkClient.class);

    private static final int SESSION_TIMEOUT = 5000;
    private static final int RETRY_SLEEP_TIMES = 3000;
    private static final int RETRY_TIMES = 3;

    private static ZkClient client;

    private CuratorFramework zk;
    private String zkAddress;

    private ZkClient(String zkAddress) {
        this.zkAddress = zkAddress;
        initialize();
    }

    private void initialize() {
        this.zk = CuratorFrameworkFactory.builder()
                .connectString(this.zkAddress)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(RETRY_SLEEP_TIMES, RETRY_TIMES))
                .build();
        this.zk.start();
    }

    public static ZkClient getInstance(String address) {
        ZkClient zkClient = null;
        synchronized (ZkClient.class) {
            if (client == null) {
                client = new ZkClient(address);
            }
            zkClient = client;
        }
        return zkClient;
    }

    public boolean createPersistentNode(String path, String data) {
        return createNode(path, data, CreateMode.PERSISTENT);

    }

    public boolean createEphemeralNode(String path, String data) {
        return createNode(path, data, CreateMode.EPHEMERAL);
    }

    public boolean createNode(String path, String data, CreateMode mode) {
        if (Strings.isNullOrEmpty(path)) {
            return false;
        }
        if (Strings.isNullOrEmpty(data)) {
            data = path;
        }
        try {
            zk.create()
                    .creatingParentsIfNeeded()
                    .withMode(mode)
                    .forPath(path, data.getBytes(Charsets.UTF_8));
            return true;
        } catch (Exception e) {
            LOG.error("ZkClient create node with path [{}] and path [{}] failed.", path, mode);
            throw new SchedulerException(ErrorCode.ZK_NODE_CREATE_EXCEPTION, e);
        }
    }

    public LeaderLatch leaderElection(String path, String id) {
        return new LeaderLatch(zk, path, id);
    }

    @Override
    public void close() throws IOException {
        CloseableUtils.closeQuietly(this.zk);
    }
}
