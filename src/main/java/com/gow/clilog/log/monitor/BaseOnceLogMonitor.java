package com.gow.clilog.log.monitor;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public abstract class BaseOnceLogMonitor extends BaseLogMonitor implements OnceLogMonitor {
    protected final OnceTimePair timePair;

    public BaseOnceLogMonitor(String monitorName, OnceTimePair timePair) {
        super(monitorName);
        this.timePair = timePair;
    }

    @Override
    public OnceTimePair time() {
        return timePair;
    }
}
