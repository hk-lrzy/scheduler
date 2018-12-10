package com.hklrzy.scheduler.common.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class CenterConfig {
    private static final Logger LOG =
            LoggerFactory.getLogger(CenterConfig.class);

    public static final String ZOOKEEPER_PATH = "zookeeper.path";

    public static final String PORT_CONFIG = "center.port";
    public static final int DEFAULT_PORT_CONFIG = 20882;


    public static final String CENTER_REGISTER_PATH = "center.register.path";
    public static final String DEFAULT_CENTER_REGISTER_PATH = "/scheduler/center/register";

    public static final String CENTER_ELECTION_PATH = "center.election.path";
    public static final String DEFAULT_CENTER_ELECTION_PATH = "/scheduler/center/election";

    public static final String DUBBO_APPLICATION = "center.application";
    public static final String DUBBO_REGISTRY = "center.registry";
    public static final String DUBBO_REGISTER_ID = "schedulerCenterRegister";
    public static final String DUBBO_SERVICE_VERSION = "1.0.0";

}
