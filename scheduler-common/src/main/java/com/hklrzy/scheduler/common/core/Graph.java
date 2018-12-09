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
     * @param vertex
     */
    void addOrUpdateTask(V vertex);

    /**
     * @param vertexes
     */
    void addOrUpdateTasks(Collection<V> vertexes);

    /**
     * 删除任务
     *
     * @param vertexKey 任务id
     */
    void removeTask(Long vertexKey);

    /**
     * 批量的删除任务
     *
     * @param vertexKeys 任务id的集合
     */
    void removeTasks(Collection<Long> vertexKeys);

}
