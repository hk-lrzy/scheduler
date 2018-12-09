package com.hklrzy.scheduler.center.graph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hklrzy.scheduler.common.bean.TaskBean;
import com.hklrzy.scheduler.common.bean.graph.GraphEdge;
import com.hklrzy.scheduler.common.bean.graph.GraphInsVertex;
import com.hklrzy.scheduler.common.bean.graph.GraphVertex;
import com.hklrzy.scheduler.common.constants.TaskFrequency;
import com.hklrzy.scheduler.common.converter.TaskBeanConverter;
import com.hklrzy.scheduler.common.core.Graph;
import com.hklrzy.scheduler.common.exception.ErrorCode;
import com.hklrzy.scheduler.common.exception.SchedulerException;
import com.hklrzy.scheduler.common.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
public class MemoryTaskGraph implements Graph<TaskBean> {
    private static final Logger LOG =
            LoggerFactory.getLogger(MemoryTaskGraph.class);

    private DirectedPseudograph<Long, GraphEdge> memoryGraph;

    private Map<Long, GraphVertex> existGraphVertex;

    public MemoryTaskGraph() {
        this.memoryGraph = new DirectedPseudograph<Long, GraphEdge>(GraphEdge.class);
        this.existGraphVertex = Maps.newConcurrentMap();
    }

    @Override
    public synchronized void addOrUpdateTask(TaskBean taskBean) {
        addOrUpdateTasks(Collections.singleton(taskBean));
    }

    @Override
    public synchronized void addOrUpdateTasks(Collection<TaskBean> vertexs) {
        //1. 初始化参数
        Map<GraphVertex, Set<GraphEdge>> backupGraphEdgeMap = Maps.newHashMap();
        Set<Long> newGraphEdges = Sets.newHashSet();

        //2. 添加点
        for (TaskBean bean : vertexs) {
            Long taskId = bean.getId();
            if (!memoryGraph.containsVertex(taskId)) {
                memoryGraph.addVertex(taskId);
                newGraphEdges.add(taskId);
            } else {
                Set<GraphEdge> backupGraphEdges = memoryGraph.incomingEdgesOf(taskId);
                backupGraphEdgeMap.put(existGraphVertex.get(taskId), backupGraphEdges);
                memoryGraph.removeAllEdges(backupGraphEdges);
            }
            existGraphVertex.put(taskId, TaskBeanConverter.toGraphVertex(bean));
        }

        DirectedAcyclicGraph<GraphInsVertex, DefaultEdge> insGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        //3. 添加边
        for (TaskBean bean : vertexs) {
            try {
                addEdges(bean, insGraph, true);
            } catch (Exception e) {
                memoryGraph.removeAllVertices(newGraphEdges);
                newGraphEdges.forEach(id -> existGraphVertex.remove(id));
                for (GraphVertex vertex : backupGraphEdgeMap.keySet()) {
                    existGraphVertex.put(vertex.getId(), vertex);
                    memoryGraph.removeAllEdges(memoryGraph.incomingEdgesOf(vertex.getId()));
                    backupGraphEdgeMap.get(vertex).forEach(backupGraphEdge -> memoryGraph.addEdge(backupGraphEdge.getParentId(), backupGraphEdge.getChildId()));
                }
                LOG.error("Failed when adding edge to the memory graph.", e);
                throw new IllegalArgumentException(e);
            }
            LOG.info("The task [{}] has added to graph.", bean.getName());
        }
        LOG.info("Add the task to graph successfully.");
    }

    private synchronized void addEdges(TaskBean bean, DirectedAcyclicGraph<GraphInsVertex, DefaultEdge> insGraph, boolean strict) {
        List<GraphEdge> graphEdges = JsonUtils.parseArray(bean.getDependencies(), GraphEdge.class);
        if (CollectionUtils.isNotEmpty(graphEdges)) {
            TaskFrequency frequency = TaskFrequency.ofCode(bean.getFrequency());
            LocalDateTime insTime = frequency.formatTaskTime(LocalDateTime.now());

            for (GraphEdge graphEdge : graphEdges) {
                graphEdge.setChildId(bean.getId());
                if (strict || existGraphVertex.containsKey(graphEdge.getParentId())) {
                    memoryGraph.addEdge(graphEdge.getParentId(), graphEdge.getChildId(), graphEdge);
                }
            }
        }
    }

    @Override
    public void removeTask(Long id) {
        removeTasks(Collections.singleton(id));
    }

    /**
     * <p>被删除的任务需要没有下游依赖才可以被删除</p>
     *
     * @param vertexKeys 任务id的集合
     * @return
     */
    @Override
    public void removeTasks(Collection<Long> vertexKeys) {
        Set<Long> childVertexKeys = Sets.newHashSet();
        for (Long vertexKey : vertexKeys) {
            Set<GraphEdge> graphEdges = memoryGraph.outgoingEdgesOf(vertexKey);
            childVertexKeys.addAll(graphEdges.stream().map(GraphEdge::getChildId).collect(Collectors.toSet()));
        }
        //1. 过滤自依赖
        childVertexKeys.removeAll(vertexKeys);
        if (CollectionUtils.isEmpty(childVertexKeys)) {
            memoryGraph.removeAllVertices(vertexKeys);
            vertexKeys.forEach(vertexKey -> existGraphVertex.remove(vertexKey));
            return;
        }
        // 2. 存在下游依赖
        LOG.error("The vertex keys [{}] are still referenced the other vertex [{}].", JsonUtils.toJSONString(vertexKeys), JsonUtils.toJSONString(childVertexKeys));
        throw new SchedulerException(ErrorCode.GRAPH_REMOVE_EXCEPTION);
    }


}
