package com.hklrzy.scheduler.center.meta;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public enum CenterRole {

    MASTER(0),

    SLAVE(1);

    private final int code;

    CenterRole(int code) {
        this.code = code;
    }
}
