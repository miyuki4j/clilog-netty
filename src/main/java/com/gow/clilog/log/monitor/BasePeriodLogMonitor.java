package com.gow.clilog.log.monitor;

/**
 * @author zhouhe
 * @date 2021/9/28
 * @description: 基本的 period log monitor,子类需要实现 time 和 monitor 函数
 */
public abstract class BasePeriodLogMonitor extends BaseLogMonitor implements PeriodLogMonitor {
    protected final PeriodTimePair timePair;

    public BasePeriodLogMonitor(String monitorName, PeriodTimePair timePair) {
        super(monitorName);
        this.timePair = timePair;
    }

    @Override
    public PeriodTimePair time() {
        return timePair;
    }
}
