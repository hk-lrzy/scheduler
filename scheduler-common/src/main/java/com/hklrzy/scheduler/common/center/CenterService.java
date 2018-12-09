package com.hklrzy.scheduler.common.center;

import com.hklrzy.scheduler.common.bean.InsRequest;
import com.hklrzy.scheduler.common.bean.TaskBean;
import com.hklrzy.scheduler.common.bean.TaskRequest;

import java.util.List;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public interface CenterService {

    List<TaskBean> getParentTaskBeans(TaskBean childTaskBean);


    List<TaskBean> getChildTaskBeans(TaskBean parentTaskBean);

    void handleTaskRequest(TaskRequest request);


    void handleInsRequest(InsRequest request);
}
