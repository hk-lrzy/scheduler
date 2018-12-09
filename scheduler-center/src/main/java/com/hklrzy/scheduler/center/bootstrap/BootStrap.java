package com.hklrzy.scheduler.center.bootstrap;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class BootStrap {

    public static void main(String[] args) throws InterruptedException {
        Config config = ConfigFactory
                .parseResources("center.properties")
                .resolve();
        ServerWrapper serverWrapper = new ServerWrapper(config);
        serverWrapper.start();
        Thread.currentThread().join();
    }
}
