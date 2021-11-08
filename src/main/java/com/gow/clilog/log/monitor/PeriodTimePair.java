package com.gow.clilog.log.monitor;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public class PeriodTimePair {
    protected final long initDelay;
    protected final long period;
    protected final TimeUnit unit;
    /**
     * 延迟执行还是阶段执行
     * 
     * true:scheduleAtFixedRate
     * 
     * false:scheduleWithFixedDelay
     */
    protected final boolean isRateScheduled;

    public PeriodTimePair(long initDelay, long period, TimeUnit unit, boolean isRateScheduled) {
        this.initDelay = initDelay;
        this.period = period;
        this.unit = unit;
        this.isRateScheduled = isRateScheduled;
    }

    public PeriodTimePair(long initDelay, long period, TimeUnit unit) {
        this(initDelay, period, unit, true);
    }

    public long getInitDelay() {
        return initDelay;
    }

    public long getPeriod() {
        return period;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public boolean isRateScheduled() {
        return isRateScheduled;
    }
}
