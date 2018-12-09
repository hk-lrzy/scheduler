package com.hklrzy.scheduler.center.bootstrap;

import com.hklrzy.scheduler.center.server.CenterServer;
import com.hklrzy.scheduler.common.configuration.CenterConfig;
import com.hklrzy.scheduler.common.core.Disposable;
import com.hklrzy.scheduler.common.zkclient.ZkClient;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class ServerWrapper implements Disposable {

    private static final Logger LOG =
            LoggerFactory.getLogger(ServerWrapper.class);

    private final Config config;

    private ZkClient zkClient;
    private CenterServer centerServer;


    public ServerWrapper(Config config) {
        this.config = config;
    }

    public void start() {
        LOG.info("The scheduler center start init.");
        register();
        startRpcServerSync();
        startDispatcherScheduler();
        LOG.info("The scheduler center init done.");

    }


    private void register() {
        this.zkClient = ZkClient.getInstance(config.getString(CenterConfig.ZOOKEEPER_PATH));
        this.centerServer = CenterServer.create(config, zkClient);
        this.centerServer.register();
    }

    private void startRpcServerSync() {
    }

    private void startDispatcherScheduler() {
    }

    @Override
    public void destroy() {

    }
}
