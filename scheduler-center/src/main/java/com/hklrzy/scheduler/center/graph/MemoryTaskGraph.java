package com.hklrzy.scheduler.center.graph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hklrzy.scheduler.common.bean.GraphEdge;
import com.hklrzy.scheduler.common.bean.GraphVertex;
import com.hklrzy.scheduler.common.bean.TaskBean;
import com.hklrzy.scheduler.common.core.Graph;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
public class MemoryTaskGraph implements Graph<TaskBean> {

    private DirectedPseudograph<Long, GraphEdge> memoryGraph;

    private Map<Long, GraphVertex> existGraphVertex;

    public MemoryTaskGraph() {
        this.memoryGraph = new DirectedPseudograph<Long, GraphEdge>(GraphEdge.class);
        this.existGraphVertex = Maps.newConcurrentMap();
    }

    public void addOrUpdateTask(TaskBean taskBean) {
        addOrUpdateTasks(Collections.singleton(taskBean));
    }

    public void addOrUpdateTasks(Collection<TaskBean> taskBeans) {
        Map<GraphVertex, Set<GraphEdge>> readyGraphMap = Maps.newHashMap();
        Set<Long> graphEdgeSets = Sets.newHashSet();
        for (TaskBean bean : taskBeans) {
            Long taskId = bean.getId();
            if (!memoryGraph.containsVertex(taskId)) {
                memoryGraph.addVertex(taskId);
                graphEdgeSets.add(taskId);
            } else {

            }

        }
    }

    public TaskBean removeTask(Long id) {
        return null;
    }
}
