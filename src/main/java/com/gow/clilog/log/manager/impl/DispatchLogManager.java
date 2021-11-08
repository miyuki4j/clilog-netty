package com.gow.clilog.log.manager.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.factory.LogServiceFactory;
import com.gow.clilog.log.manager.AbstractLogManager;
import com.gow.clilog.log.monitor.OnceTimePair;
import com.gow.clilog.log.service.LogService;

/**
 * @author zhouhe
 * @date 2021/9/29
 */
public class DispatchLogManager extends AbstractLogManager {

    public DispatchLogManager(String managerName) {
        super(managerName);
    }

    public DispatchLogManager() {
        this("DispatchLogManager");
    }

    /**
     * 转发器实现初始化服务
     * 
     * @param loggerName
     * @return
     * @throws Exception
     */

    @Override
    public LogService init(String loggerName) throws Exception {
        String serviceName = this.calcServiceName(loggerName);
        LogService logService = serviceMap.get(serviceName);
        if (logService != null) {
            return logService;
        }
        logService = LogServiceFactory.createDispatchService(serviceName, serviceName, this, nextLogManger);
        serviceMap.put(serviceName, logService);
        return logService;
    }

    @Override
    protected String calcServiceName(final String loggerName) {
        String tName = Thread.currentThread().getName();
        return tName + "-Dispatch";
    }

    /**
     * 线程安全
     * 
     * @param loggerName
     * @return
     */
    @Override
    protected LogService selectOutputService(final String loggerName) throws Exception {
        LogService logService = queryService(loggerName);
        if (logService == null) {
            logService = initAndStart(loggerName);
        }
        return logService;
    }

    @Override
    public boolean offer(LogData logData) throws Exception {
        if (!isRunning()) {
            return false;
        }
        LogService service = selectOutputService(logData.loggerName());
        return service.offer(logData);

    }

    @Override
    public boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception {
        if (!isRunning()) {
            return false;
        }
        LogService service = selectOutputService(logData.loggerName());
        return service.offer(logData, timeout, unit);
    }

    @Override
    public Set<LogService> tryReload(Set<String> loggerNameSet) throws Exception {
        return null;
    }

    @Override
    public boolean isDirectable() {
        return false;
    }

    /**
     * 不做注册
     * 
     * @throws Exception
     */
    @Override
    public void customRegister() throws Exception {

    }

    @Override
    public void oneKeyOpen() throws Exception {
        // init next manager
        initNextManager(new Dispatch2DirectLogManager());
        // start
        start();
        // 自定义注册
        customRegister();
        // next manager one key open
        nextManager().oneKeyOpen();
    }

    @Override
    public void oneKeyClose() throws Exception {
        stop();
        nextManager().oneKeyClose();
        destory();
    }

    /**
     * 转发模式与直接模式的桥梁
     */
    public static class Dispatch2DirectLogManager extends DirectLogManager {
        public Dispatch2DirectLogManager(String managerName) {
            super(managerName);
        }

        public Dispatch2DirectLogManager() {
            super("Dispatch2DirectLogManager");
        }

        @Override
        public LogService init(String loggerName) throws Exception {
            assert loggerName != null && !loggerName.isEmpty();
            String serviceName = calcServiceName(loggerName);
            if (serviceMap.containsKey(serviceName)) {
                throw new RuntimeException(managerName + " init service duplicated. logger name=" + loggerName);
            }
            LogService service = LogServiceFactory.createDispatch2DirectService(loggerName, this,
                new OnceTimePair(3L, TimeUnit.SECONDS));
            serviceMap.put(serviceName, service);
            return service;
        }
    }
}
