package com.hklrzy.scheduler.common.constants;

import java.io.Serializable;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public enum TaskRequestType implements Serializable {
    

    /**
     * 开启
     */
    OPEN,


    /**
     * 上线
     */
    DEPLOY,

    /**
     * 关闭
     */
    CLOSE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 生成对应的实例信息
     */
    GENE_INSTANCE,

    /**
     * 取消所有实例
     */
    CANCEL_ALL_INSTANCE,
}
