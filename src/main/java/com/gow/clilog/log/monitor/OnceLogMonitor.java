package com.gow.clilog.log.monitor;

/**
 * @author zhouhe
 * @date 2021/9/28
 * @description: 运行一次的log监控器
 */
public interface OnceLogMonitor extends LogMonitor {
    OnceTimePair time();
}
