package com.hklrzy.scheduler.center.server;

import com.google.common.base.Strings;
import com.hklrzy.scheduler.center.meta.CenterRole;
import com.hklrzy.scheduler.common.configuration.CenterConfig;
import com.hklrzy.scheduler.common.exception.ErrorCode;
import com.hklrzy.scheduler.common.exception.SchedulerException;
import com.hklrzy.scheduler.common.utils.NetUtils;
import com.hklrzy.scheduler.common.zkclient.ZkClient;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class CenterServer implements Disposable {
    private static final Logger LOG =
            LoggerFactory.getLogger(CenterServer.class);

    private final ZkClient zkClient;
    private final int port;
    private final String address;
    private final String registerPath;
    private final String electionPath;

    private LeaderLatch latch;
    private CenterRole role;
    private String leader;
    private List<String> slaves;


    /**
     * 创建一个调度中心的服务
     *
     * @param config   配置信息
     * @param zkClient zk客户端
     * @return
     */
    public static CenterServer create(Config config, ZkClient zkClient) {
        return new CenterServer(zkClient, NetUtils.getLocalAddress(), config.getInt(CenterConfig.PORT_CONFIG));
    }

    public CenterServer(ZkClient zkClient, String address, int port) {
        this.address = address;
        this.port = port;
        this.zkClient = zkClient;
        this.registerPath = CenterConfig.DEFAULT_CENTER_REGISTER_PATH;
        this.electionPath = CenterConfig.DEFAULT_CENTER_ELECTION_PATH;
    }

    /**
     * 注册服务，并选举出主要负责服务的leader节点
     */
    public void register() {
        String path = getRegisterPath();
        this.latch = zkClient.leaderElection(this.electionPath, path);
        this.latch.addListener(new AcquireMetaLeaderLatchListener(path));
        try {
            this.latch.start();
            Thread.sleep(200);
        } catch (Exception e) {
            LOG.error("Start election center failed.");
            throw new SchedulerException(ErrorCode.LEADER_ELECTION_EXCEPTION);
        }
    }


    private CenterRole getRole(boolean leaderShip) {
        return leaderShip ? CenterRole.MASTER : CenterRole.SLAVE;
    }

    private String getRegisterPath() {
        if (!NetUtils.isValid(this.address, this.port)) {
            throw new SchedulerException(ErrorCode.PARAMETER_INVAILED_EXCEPTION);
        }
        return StringUtils.join(this.address, ":", this.port);
    }

    private static String append(String base, String path) {
        if (Strings.isNullOrEmpty(base)) {
            throw new SchedulerException(ErrorCode.PARAMETER_INVAILED_EXCEPTION);
        }
        StringBuilder builder = new StringBuilder(base);
        if (!base.endsWith(File.separator)) {
            builder.append(File.separator);
        }
        builder.append(path);
        return builder.toString();
    }

    @Override
    public void export() {

    }

    @Override
    public void close() {

    }


    /**
     * 用于选举
     */
    private class AcquireMetaLeaderLatchListener implements LeaderLatchListener {

        private String path;

        public AcquireMetaLeaderLatchListener(String path) {
            this.path = path;
        }

        @Override
        public void isLeader() {
            acquireMeta(append(electionPath, path), path);
        }

        @Override
        public void notLeader() {
            acquireMeta(append(registerPath, path), path);
        }

        private void acquireMeta(String path, String data) {
            zkClient.createEphemeralNode(path, data);
            role = getRole(latch.hasLeadership());
            leader = ZkClient.getLatchLeaderId(latch);
            slaves = zkClient.getChildNode(registerPath);
            LOG.info("The center [{}] has become role [{}].", path, role);
        }
    }
}
