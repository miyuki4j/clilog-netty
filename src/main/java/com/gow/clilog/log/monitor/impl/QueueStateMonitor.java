package com.gow.clilog.log.monitor.impl;

import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.BasePeriodLogMonitor;
import com.gow.clilog.log.monitor.PeriodTimePair;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.util.SystemLogger;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public class QueueStateMonitor extends BasePeriodLogMonitor {

    /** 监控的log manager */
    protected LogManager monitorManager;

    public QueueStateMonitor(LogManager monitorManager, PeriodTimePair timePair) {
        super(monitorManager.managerName()+"-QueueStateMonitor", timePair);
        this.monitorManager = monitorManager;
    }

    @Override
    protected void monitor() throws Exception {
        try {
            for (LogService service : this.monitorManager.services()) {
                String state = service.serviceState();
                SystemLogger.info(state);
            }
        } catch (Throwable t) {
            SystemLogger.error("QueueStateMonitor >>> view queue state error. ", t);
        }
    }

    @Override
    public void destory() throws Exception {
        super.destory();
        this.monitorManager = null;
    }
}
