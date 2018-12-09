package com.hklrzy.scheduler.common.exception;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class SchedulerException extends RuntimeException {

    private int errorCode;

    public SchedulerException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public SchedulerException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.getDescription(), throwable);
        this.errorCode = errorCode.getCode();
    }
}
