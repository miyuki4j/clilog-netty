package com.gow.clilog.log.manager;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.factory.LogMonitorFactory;
import com.gow.clilog.log.monitor.*;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.conf.SystemConfig;
import com.gow.clilog.util.SystemLogger;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author zhouhe
 * @date 2021/9/28
 * @description: 抽象任务管理器
 */
public abstract class AbstractLogManager implements LogManager {

    protected final String managerName;

    protected volatile boolean running = false;
    /**
     * monitor part key: {@link LogMonitor#uniqName()} ,
     */
    protected ConcurrentMap<String, Future<?>> futures = new ConcurrentHashMap<>();
    protected ConcurrentMap<String, LogMonitor> monitorMap = new ConcurrentHashMap<>();
    protected ScheduledExecutorService monitorService;

    /**
     * service part
     * 
     * key: {@linkplain #calcServiceName(String loggerName)}
     */
    protected Map<String, LogService> serviceMap = new ConcurrentHashMap<>();

    protected LogManager nextLogManger;// 后继log manager

    /**
     * @param managerName
     */
    protected AbstractLogManager(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public Set<LogService> init(Set<String> loggerNameSet) throws Exception {
        Set<LogService> services = new LinkedHashSet<>();
        for (String loggerName : loggerNameSet) {
            services.add(init(loggerName));
        }
        return services;
    }

    @Override
    public String managerName() {
        return managerName;
    }

    @Override
    public synchronized void start() throws Exception {
        if (running) {
            return;
        }
        for (Map.Entry<String, LogService> entry : serviceMap.entrySet()) {
            String loggerName = entry.getKey();
            LogService logService = entry.getValue();
            logService.start();
        }
        // init executor
        this.monitorService = initMonitorExecutor();
        running = true;
    }

    @Override
    public synchronized void stop() throws Exception {
        if (!running) {
            return;
        }
        running = false;
        // stop all monitor task
        for (Map.Entry<String, Future<?>> entry : futures.entrySet()) {
            Future<?> future = entry.getValue();
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
        if (this.monitorService != null) {
            monitorService.shutdown();
        }
        for (LogService logService : serviceMap.values()) {
            logService.stop();
        }
    }

    @Override
    public synchronized void destory() throws Exception {
        futures.clear();
        for (LogService service : serviceMap.values()) {
            service.destroy();
        }
        serviceMap.clear();
        for (LogMonitor monitor : monitorMap.values()) {
            monitor.destory();
        }
        monitorMap.clear();
        monitorService.awaitTermination(10L, TimeUnit.SECONDS);
        monitorService = null;
        nextLogManger = null;
    }

    @Override
    public Parentable parent() {
        return null;
    }

    @Override
    public void parent(Parentable p) {

    }

    @Override
    public void unParent() {

    }

    @Override
    public void initNextManager(LogManager nextManager) throws Exception {
        assert nextManager != null;
        this.nextLogManger = nextManager;
    }

    @Override
    public LogManager nextManager() {
        return nextLogManger;
    }
    // ******************************************** TODO other method ********************************************

    @Override
    public LogService queryService(String loggerName) {
        String serviceName = this.calcServiceName(loggerName);
        return serviceMap.get(serviceName);
    }

    @Override
    public boolean addService(LogService logService) throws Exception {
        String serviceName = this.calcServiceName(logService.loggerName());
        if (serviceMap.containsKey(serviceName)) {
            return false;
        }
        serviceMap.put(serviceName, logService);
        return true;
    }

    @Override
    public LogService removeService(String loggerName) throws Exception {
        String serviceName = this.calcServiceName(loggerName);
        return serviceMap.remove(serviceName);
    }

    @Override
    public boolean removeService(LogService logService) throws Exception {
        return removeService(logService.loggerName()) != null;
    }

    @Override
    public Collection<LogService> services() {
        return Collections.unmodifiableCollection(serviceMap.values());
    }

    @Override
    public Collection<LogMonitor> monitors() {
        return Collections.unmodifiableCollection(monitorMap.values());
    }

    @Override
    public boolean registerLogMonitor(LogMonitor monitor) throws Exception {
        if (monitor == null) {
            throw new NullPointerException(managerName + " register monitor is null.");
        }
        String monitorUniqName = monitor.uniqName();
        if (monitorMap.containsKey(monitorUniqName)) {
            throw new RuntimeException(managerName + " register monitor duplicated. uniqname=" + monitorUniqName);
        }
        Future<?> future = null;
        boolean success = true;
        try {
            if (monitor instanceof OnceLogMonitor) {
                OnceLogMonitor onceLogMonitor = (OnceLogMonitor)monitor;
                OnceTimePair time = onceLogMonitor.time();
                this.monitorService.schedule(monitor, time.getDelay(), time.getUnit());
            } else if (monitor instanceof PeriodLogMonitor) {
                PeriodLogMonitor periodLogMonitor = (PeriodLogMonitor)monitor;
                PeriodTimePair time = periodLogMonitor.time();
                if (time.isRateScheduled()) {
                    future = this.monitorService.scheduleAtFixedRate(monitor, time.getInitDelay(), time.getPeriod(),
                        time.getUnit());
                } else {
                    future = this.monitorService.scheduleWithFixedDelay(monitor, time.getInitDelay(), time.getPeriod(),
                        time.getUnit());
                }

            } else {
                future = monitorService.submit(monitor);
            }
        } catch (Exception e) {
            success = false;
            throw e;
        } catch (Throwable t) {
            success = false;
            throw new RuntimeException(t);
        } finally {
            if (success) {
                futures.put(monitorUniqName, future);
                monitorMap.put(monitorUniqName, monitor);
                try {
                    monitor.init();
                } catch (Throwable e) {
                    futures.remove(monitorUniqName);
                    monitorMap.remove(monitorUniqName);
                    if (future != null) {
                        future.cancel(true);
                    }
                    success = false;
                }
            } else {
                if (future != null) {
                    future.cancel(true);
                }
            }
        }
        return success;
    }

    @Override
    public boolean registerLogMonitor(Collection<LogMonitor> monitors) throws Exception {
        for (LogMonitor monitor : monitors) {
            if (!registerLogMonitor(monitor)) {
                throw new RuntimeException("monitor [" + monitor.uniqName() + "] register failed.");
            }
        }
        return true;
    }

    @Override
    public void customRegister() throws Exception {
        SystemLogger.info(managerName + " >>> customRegister.");
        LogMonitor queueStateMonitor = LogMonitorFactory
            .createQueueStateMonitor(SystemConfig.getInstance().getLogQueueGap(), TimeUnit.SECONDS, this);
        if (!registerLogMonitor(queueStateMonitor)) {
            throw new RuntimeException("queueStateMonitor register failed.");
        }
    }

    @Override
    public boolean offer(LogData logData) throws Exception {
        if (!isRunning()) {
            return false;
        }
        if (nextLogManger != null) {
            return nextLogManger.offer(logData);
        } else {
            LogService service = selectOutputService(logData.loggerName());
            return service.offer(logData);
        }
    }

    @Override
    public boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception {
        if (!isRunning()) {
            return false;
        }
        if (nextLogManger != null) {
            return nextLogManger.offer(logData, timeout, unit);
        } else {
            LogService service = selectOutputService(logData.loggerName());
            return service.offer(logData, timeout, unit);
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    // *********************************** sub class implements *********************************** //

    /**
     * 提供一个默认实现，子类可以根据需求,自定义修改
     * 
     * @return
     */
    protected ScheduledExecutorService initMonitorExecutor() throws Exception {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread monitorThread = new Thread(r, managerName() + "-Monitor") {
                    @Override
                    public void run() {
                        super.run();
                        SystemLogger.info(getName() + " stop.");
                    }
                };
                monitorThread.setDaemon(true);
                return monitorThread;
            }
        });
        return service;
    }

    /**
     * 子类根据需要修改
     * 
     * @param loggerName
     * @return
     */
    protected String calcServiceName(final String loggerName) {
        return loggerName + LogService.SERVICE_NAME_SUFFIX;
    }

    /**
     * 找到输出的 log service,子类根据需要自己复写
     * 
     * @param loggerName
     * @return
     */
    protected LogService selectOutputService(String loggerName) throws Exception {
        LogService service = serviceMap.get(calcServiceName(loggerName));
        if (service == null) {
            String defaultLogName = calcServiceName(SystemConfig.getInstance().getLogDefaultName());
            if (defaultLogName != null && !defaultLogName.isEmpty()) {
                service = serviceMap.get(defaultLogName);
            }
        }
        return service;
    }

    /**
     * 子类根据需要修改,以及是否使用
     * 
     * @param loggerName
     * @return
     * @throws Exception
     */
    protected LogService initAndStart(String loggerName) throws Exception {
        LogService service = init(loggerName);
        service.start();
        return service;
    }
}
