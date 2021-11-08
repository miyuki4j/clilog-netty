package com.gow.clilog.log.monitor;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public class OnceTimePair {
    protected final long delay;
    protected final TimeUnit unit;

    public OnceTimePair(long time, TimeUnit unit) {
        this.delay = Math.max(0, time);
        this.unit = unit;
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getUnit() {
        return unit;
    }
}
