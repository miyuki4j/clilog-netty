package com.gow.clilog.log.queue.impl;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.queue.AbstractLogQueue;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * @author zhouhe
 * @date 2021/9/27
 */
public class DefaultLogQueue extends AbstractLogQueue {

    public DefaultLogQueue(String queueName, String loggerName, BlockingQueue<LogData> logQueue) {
        super(queueName, loggerName, logQueue);
    }

    public DefaultLogQueue(String loggerName, Queue<LogData> logQueue) {
        super(loggerName, logQueue);
    }

    @Override
    protected void initOtherParam() throws Exception {
        super.initOtherParam();
    }
}
