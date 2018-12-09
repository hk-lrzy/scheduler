package com.hklrzy.scheduler.common.bean.graph;

import com.hklrzy.scheduler.common.constants.TaskOffsetType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
@Data
@EqualsAndHashCode(of = {"parentId", "childId"})
public class GraphEdge {

    private Long parentId;

    private Long childId;

    private Integer[] offsets;

    private TaskOffsetType offsetType;
}
