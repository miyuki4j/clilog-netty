package com.gow.clilog.log.monitor;

import com.gow.clilog.log.manager.LogManager;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public interface LogMonitor extends Runnable {
    String MONITOR_NAME_SUFFIX = "-LogMonitor";

    /**
     * @see {@linkplain LogManager#registerLogMonitor(LogMonitor)}
     * 
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * @see {@linkplain LogManager#destory()}
     *
     * @throws Exception
     */
    void destory() throws Exception;

    /**
     *
     * @return
     */
    String monitorName();

    /**
     *
     * @return
     */
    long uniqId();

    /**
     *
     * @return
     */
    String uniqName();

}
