package com.hklrzy.scheduler.common.center;

import com.hklrzy.scheduler.common.bean.TaskRecord;
import com.hklrzy.scheduler.common.bean.request.InsRequest;
import com.hklrzy.scheduler.common.bean.request.TaskRequest;

import java.util.List;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public interface CenterService {

    List<TaskRecord> getParentTaskBeans(TaskRecord childTaskRecord);


    List<TaskRecord> getChildTaskBeans(TaskRecord parentTaskRecord);

    void handleTaskRequest(TaskRequest request);

    void handleInsRequest(InsRequest request);
}
