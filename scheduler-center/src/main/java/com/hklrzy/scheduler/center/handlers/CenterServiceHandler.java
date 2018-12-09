package com.hklrzy.scheduler.center.handlers;

import com.google.common.base.Preconditions;
import com.hklrzy.scheduler.center.server.CenterServer;
import com.hklrzy.scheduler.common.bean.InsRequest;
import com.hklrzy.scheduler.common.bean.TaskBean;
import com.hklrzy.scheduler.common.bean.TaskRequest;
import com.hklrzy.scheduler.common.center.CenterService;
import com.hklrzy.scheduler.common.core.Graph;
import com.hklrzy.scheduler.common.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class CenterServiceHandler implements CenterService {
    private static final Logger LOG =
            LoggerFactory.getLogger(CenterServiceHandler.class);

    private Graph<TaskBean> taskBeanGraph;
    private CenterServer centerServer;


    @Override
    public List<TaskBean> getParentTaskBeans(TaskBean childTaskBean) {
        return null;
    }

    @Override
    public List<TaskBean> getChildTaskBeans(TaskBean parentTaskBean) {
        return null;
    }

    @Override
    public void handleTaskRequest(TaskRequest request) {
        Preconditions.checkNotNull(request, ErrorCode.COMMON_MISSING_PARAMETER_EXCEPTION.getDescription());

        LOG.info("Start handle the task request.");
        switch (request.getRequestType()) {
            case OPEN:
            case DEPLOY:
                taskBeanGraph.addOrUpdateTasks(request.getRequestLists());
                break;
            case CLOSE:
            case DELETE:
                Set<Long> vertexKeys = request.getRequestLists()
                        .stream()
                        .map(TaskBean::getId)
                        .collect(Collectors.toSet());
                taskBeanGraph.removeTasks(vertexKeys);
                break;
            default:
                LOG.warn("Unknown task request type [{}].", request.getRequestType());
        }
    }

    @Override
    public void handleInsRequest(InsRequest request) {
    }


}
