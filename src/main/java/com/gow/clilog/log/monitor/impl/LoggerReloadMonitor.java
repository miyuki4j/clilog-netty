package com.gow.clilog.log.monitor.impl;

import com.gow.clilog.conf.ServerConstant;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.BasePeriodLogMonitor;
import com.gow.clilog.log.monitor.PeriodTimePair;
import com.gow.clilog.conf.ResourcesManager;
import com.gow.clilog.util.PropertiesUtil;
import com.gow.clilog.util.SystemLogger;
import com.gow.clilog.util.SystemUtil;

import java.util.Properties;
import java.util.Set;

/**
 * @author zhouhe
 * @date 2021/9/28
 * @description: 创建一个 log服务 监视器
 */
public class LoggerReloadMonitor extends BasePeriodLogMonitor {

    protected LogManager reloadMonitorManager;

    public LoggerReloadMonitor(LogManager reloadMonitorManager, PeriodTimePair timePair) {
        super("LoggerReloadMonitor", timePair);
        this.reloadMonitorManager = reloadMonitorManager;
    }

    @Override
    protected void monitor() throws Exception {
        try {
            Properties prop = PropertiesUtil.loadProperties(ResourcesManager.getInstance().getConfigPropPath());
            Set<String> loggerNameSet =
                SystemUtil.parseStrSet(prop.getProperty(ServerConstant.ENV_UPLOAD_LOG_SET, ""), ";");
            this.reloadMonitorManager.tryReload(loggerNameSet);
        } catch (Throwable e) {
            SystemLogger.error("LoggerReloadMonitor >>> try reload error.", e);
        }
    }

    @Override
    public void destory() throws Exception {
        super.destory();
        this.reloadMonitorManager = null;
    }
}
