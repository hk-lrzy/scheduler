package com.hklrzy.scheduler.common.core;

import java.util.Collection;

/**
 * Created 2018/12/6.
 * 存储上下游依赖的图的抽象接口
 *
 * @author ke.hao
 */
public interface Graph<V> {

    /**
     * @param v
     */
    void addOrUpdateTask(V v);


    /**
     * @param vCollection
     */
    void addOrUpdateTasks(Collection<V> vCollection);

    /**
     * 删除任务
     *
     * @param id 任务id
     * @return 被删除的任务
     */
    V removeTask(Long id);

}
