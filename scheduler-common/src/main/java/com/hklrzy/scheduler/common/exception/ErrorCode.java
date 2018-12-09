package com.hklrzy.scheduler.common.exception;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class ErrorCode {

    public static final ErrorCode COMMON_MISSING_PARAMETER_EXCEPTION = ErrorCode.create(1, "缺少必要参数");
    public static final ErrorCode SLAVE_NOT_ALLOW_HANDLE_REQUEST_EXCEPTION = ErrorCode.create(2, "slave不能处理事务");
    public static final ErrorCode ZK_NODE_CREATE_EXCEPTION = ErrorCode.create(3, "创建zk节点异常");
    public static final ErrorCode PARAMETER_INVAILED_EXCEPTION = ErrorCode.create(4, "参数不合法");
    public static final ErrorCode LEADER_ELECTION_EXCEPTION = ErrorCode.create(5, "选举异常");


    public static final ErrorCode GRAPH_REMOVE_EXCEPTION = ErrorCode.create(100, "移除图元素异常");

    private final int code;

    private final String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private static ErrorCode create(int code, String message) {
        return new ErrorCode(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
