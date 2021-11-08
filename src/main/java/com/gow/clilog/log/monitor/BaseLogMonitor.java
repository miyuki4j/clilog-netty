package com.gow.clilog.log.monitor;

import com.gow.clilog.util.SystemLogger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public abstract class BaseLogMonitor implements LogMonitor {
    public static AtomicLong monitorCnt = new AtomicLong();

    protected final String sourceMonitorName;

    protected final String monitorName;
    protected final long uniqId;

    public BaseLogMonitor(String monitorName) {
        this.sourceMonitorName = monitorName;
        this.monitorName = monitorName;
        uniqId = monitorCnt.incrementAndGet();
    }

    @Override
    public void init() throws Exception {
        SystemLogger.info(uniqName() + " init.");
    }

    @Override
    public void destory() throws Exception {
        SystemLogger.info(uniqName() + " destory.");
    }

    @Override
    public String monitorName() {
        return monitorName;
    }

    @Override
    public long uniqId() {
        return uniqId;
    }

    public String uniqName() {
        return monitorName() + "-" + uniqId();
    }

    @Override
    public void run() {
        try {
            monitor();
        } catch (Throwable e) {
            // skip
        }
    }

    @Override
    public int hashCode() {
        return monitorName().hashCode() + (int)uniqId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BaseLogMonitor))
            return false;
        BaseLogMonitor that = (BaseLogMonitor)o;
        return uniqId == that.uniqId && monitorName.equals(that.monitorName);
    }

    // ********************************************** sub class implements ************************************* //

    /**
     * 监控任务
     *
     * @throws Exception
     */
    protected abstract void monitor() throws Exception;
}
