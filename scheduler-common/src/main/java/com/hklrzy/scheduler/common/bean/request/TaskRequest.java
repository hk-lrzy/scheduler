package com.hklrzy.scheduler.common.bean.request;

import com.hklrzy.scheduler.common.bean.TaskRecord;
import com.hklrzy.scheduler.common.constants.TaskRequestType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
@Getter
@Setter
public class TaskRequest implements Serializable {

    private static final long serialVersionUID = -8504263141218328875L;

    private TaskRequestType requestType;

    private List<TaskRecord> requestLists;
}
