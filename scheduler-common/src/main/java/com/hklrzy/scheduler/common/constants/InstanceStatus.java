package com.hklrzy.scheduler.common.constants;

/**
 * Created 2019-01-08.
 *
 * @author ke.hao
 */
public enum InstanceStatus {

    NOT_EXIST(0),
    INIT(10),
    PENDING_PARENT_DEPENDENY(20),
    PENDING_TIME(30),
    PENDING_RESOURCE(40),
    SUBMITTED(50),
    RUNNING(60),
    SUCCEED(70),
    FAILED(80),
    TRIGGER_CHILD_DEPENDENCY(90),
    CANCELED(100),
    ABORTED(101);

    int code;

    InstanceStatus(int code) {
        this.code = code;
    }
}
