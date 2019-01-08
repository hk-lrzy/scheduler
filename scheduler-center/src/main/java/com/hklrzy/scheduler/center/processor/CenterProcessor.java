package com.hklrzy.scheduler.center.processor;

import com.hklrzy.scheduler.center.server.CenterServer;
import com.hklrzy.scheduler.common.bean.TaskRecord;
import com.hklrzy.scheduler.common.core.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class CenterProcessor {
    private static final Logger logger =
            LoggerFactory.getLogger(CenterProcessor.class);

    private Graph<TaskRecord> recordGraph;
    private CenterServer centerServer;

    public static CenterProcessor getInstance() {
        return new CenterProcessor();
    }

    private CenterProcessor() {

    }
}
