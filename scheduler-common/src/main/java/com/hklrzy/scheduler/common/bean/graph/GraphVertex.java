package com.hklrzy.scheduler.common.bean.graph;

import com.cronutils.model.Cron;
import com.hklrzy.scheduler.common.constants.TaskDependencyType;
import com.hklrzy.scheduler.common.constants.TaskFrequency;
import com.hklrzy.scheduler.common.constants.TaskPriority;
import com.hklrzy.scheduler.common.constants.TaskType;
import lombok.Data;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
@Data
public class GraphVertex {

    private Long id;

    private String name;

    private Integer version;

    private Long owner;

    private TaskPriority priority;

    private TaskFrequency frequency;

    private Cron cron;

    private TaskType type;

    private TaskDependencyType dependencyType;

    private Long projectId;

}
