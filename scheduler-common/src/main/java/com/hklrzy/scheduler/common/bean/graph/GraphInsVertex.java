package com.hklrzy.scheduler.common.bean.graph;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
@Data
@EqualsAndHashCode(of = {"taskId", "taskTime"})
public class GraphInsVertex {

    private Long taskId;

    private LocalDateTime taskTime;
}
