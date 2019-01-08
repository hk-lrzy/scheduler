package com.hklrzy.scheduler.common.bean;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Created 2019-01-08.
 *
 * @author ke.hao
 */
@Data
@Builder
@EqualsAndHashCode(of = {"taskId", "taskTime"})
@AllArgsConstructor
@NoArgsConstructor
public class TaskInstanceRecord {

    private Long taskId;

    private LocalDateTime taskTime;

    private Long projectId;

    private Long name;
}
