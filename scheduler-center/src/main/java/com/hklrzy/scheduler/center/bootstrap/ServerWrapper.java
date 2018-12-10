package com.hklrzy.scheduler.center.bootstrap;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.hklrzy.scheduler.center.handlers.CenterServiceHandler;
import com.hklrzy.scheduler.center.server.CenterServer;
import com.hklrzy.scheduler.common.center.CenterService;
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
    private ServiceConfig<CenterService> serviceConfig;


    public ServerWrapper(Config config) {
        this.config = config;
    }

    public void start() {
        LOG.info("The scheduler center start init.");
        register();
        createMasterService();
        createMulticastService();
        startDispatcherScheduler();
        LOG.info("The scheduler center init done.");

    }

    private void register() {
        this.zkClient = ZkClient.getInstance(config.getString(CenterConfig.ZOOKEEPER_PATH));
        this.centerServer = CenterServer.create(config, zkClient);
        this.centerServer.register();
    }


    private void createMasterService() {
        ApplicationConfig applicationConfig = new ApplicationConfig(config.getString(CenterConfig.DUBBO_APPLICATION));
        RegistryConfig registryConfig = new RegistryConfig(config.getString(CenterConfig.DUBBO_REGISTRY));
        registryConfig.setGroup(CenterConfig.DUBBO_APPLICATION);
        registryConfig.setProtocol("zookeeper");
        registryConfig.setId(CenterConfig.DUBBO_REGISTER_ID);
        this.serviceConfig = new ServiceConfig<>();

        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setInterface(CenterService.class);
        serviceConfig.setRef(CenterServiceHandler.getInstance());
        serviceConfig.setVersion(CenterConfig.DUBBO_SERVICE_VERSION);
    }

    private void createMulticastService() {

    }


    private void startRpcServerSync() {

    }

    private void startDispatcherScheduler() {
    }

    @Override
    public void destroy() {

    }
}
