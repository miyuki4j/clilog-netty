package com.gow.clilog.log.factory;

import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.LogMonitor;
import com.gow.clilog.log.monitor.PeriodTimePair;
import com.gow.clilog.log.monitor.impl.LoggerReloadMonitor;
import com.gow.clilog.log.monitor.impl.QueueStateMonitor;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/28
 * @description: log 监视器工厂
 */
public final class LogMonitorFactory {
    /**
     * 创建一个队列状态监视器
     * 
     * @param gap
     * @param unit
     * @param logManager
     * @return
     */
    public static LogMonitor createQueueStateMonitor(long gap, TimeUnit unit, final LogManager logManager) {
        return new QueueStateMonitor(logManager, new PeriodTimePair(gap, gap, unit));
    }

    /**
     * 创建一个服务热加载监视器
     * 
     * @param gap
     * @param unit
     * @param logManager
     * @return
     */
    public static LogMonitor createReloadMonitor(long gap, TimeUnit unit, final LogManager logManager) {
        return new LoggerReloadMonitor(logManager, new PeriodTimePair(gap, gap, unit));
    }
}
