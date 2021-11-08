package com.gow.clilog.log.manager.impl;

import com.gow.clilog.log.factory.LogMonitorFactory;
import com.gow.clilog.log.factory.LogServiceFactory;
import com.gow.clilog.log.manager.AbstractLogManager;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.LogMonitor;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.conf.SystemConfig;
import com.gow.clilog.util.SystemLogger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @description: 直接处理模式 log manager
 */
public class DirectLogManager extends AbstractLogManager {

    /**
     * @param managerName
     */
    public DirectLogManager(String managerName) {
        super(managerName);
    }

    public DirectLogManager() {
        this("DirectLogManager");
    }

    @Override
    public void initNextManager(LogManager nextManager) throws Exception {
        throw new UnsupportedOperationException("DirectLogManager not support init the next manager.");
    }

    @Override
    public LogService init(String loggerName) throws Exception {
        assert loggerName != null && !loggerName.isEmpty();
        String serviceName = this.calcServiceName(loggerName);
        if (serviceMap.containsKey(serviceName)) {
            throw new RuntimeException(managerName + " init service duplicated. logger name=" + loggerName);
        }
        LogService service = LogServiceFactory.createDirectService(loggerName, this);
        serviceMap.put(serviceName, service);
        return service;
    }

    /**
     * 这个只实现动态添加logger，一般不会删除如果需要在增加
     * 
     * @param loggerNameSet
     * @throws Exception
     */
    @Override
    public synchronized Set<LogService> tryReload(Set<String> loggerNameSet) throws Exception {
        Set<LogService> reloadSet = new LinkedHashSet<>();
        if (!isRunning() || loggerNameSet == null || loggerNameSet.isEmpty()) {
            return reloadSet;
        }
        Set<String> add = new LinkedHashSet<String>();
        for (String loggerName : loggerNameSet) {
            if (queryService(loggerName) == null) {
                add.add(loggerName);
            }
        }
        for (String addLoggerName : add) {
            reloadSet.add(initAndStart(addLoggerName));
        }
        return reloadSet;
    }

    @Override
    public void customRegister() throws Exception {
        super.customRegister();
        LogMonitor reloadMonitor =
            LogMonitorFactory.createReloadMonitor(SystemConfig.getInstance().getLogReloadGap(), TimeUnit.SECONDS, this);
        if (!registerLogMonitor(reloadMonitor)) {
            throw new RuntimeException("reloadMonitor register failed.");
        }
        SystemLogger.info(managerName + " customRegister success.");
    }

    @Override
    public boolean isDirectable() {
        return true;
    }

    /**
     * 一键启动
     * 
     * @throws Exception
     */
    @Override
    public void oneKeyOpen() throws Exception {
        // 初始化
        init(SystemConfig.getInstance().getLogNameSet());
        // 启动
        start();
        // 注册
        customRegister();
    }

    @Override
    public void oneKeyClose() throws Exception {
        // 停止
        stop();
        // 销毁
        destory();
    }
}
