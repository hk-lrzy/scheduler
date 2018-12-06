package com.hklrzy.scheduler.common.bean;

import com.hklrzy.scheduler.common.constants.TaskOffsetType;

/**
 * Created 2018/12/6.
 *
 * @author ke.hao
 */
public class GraphEdge {

    private Long parentId;

    private Long childId;

    private Integer[] offsets;

    private TaskOffsetType offsetType;
}
