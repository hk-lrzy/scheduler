package com.hklrzy.scheduler.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
@Data
public class TaskRecord implements Serializable {

    private static final long serialVersionUID = 110149993973396573L;

    private Long id;

    private Long projectId;

    private Integer version;

    private Integer type;

    private Integer status;

    private Integer scheduleType;

    private Integer priority;

    private Integer dependencyType;

    private Integer frequency;

    private String name;

    private String cronExpression;

    private String dependencies;

    private String config;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
