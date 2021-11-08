package com.gow.clilog.log;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.log.type.LogDataConverterEnum;
import com.gow.clilog.log.type.LogHandleStrategyEnum;
import com.gow.clilog.conf.SystemConfig;
import com.gow.clilog.util.SystemLogger;

/**
 * @author zhouhe
 * @date 2021/9/29
 */
public final class LogCenter {
    private static LogCenter center = new LogCenter();
    private LogManager globalLogManager;
    private LogHandleStrategyEnum globalLogStrategy;
    private LogDataConverterEnum globalLogConverter;
    private boolean running;

    private LogCenter() {

    }

    public static LogCenter getInstance() {
        return center;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * 开启服务
     *
     * @throws Exception
     */
    public synchronized void start() throws Exception {
        if (running) {
            return;
        }
        this.globalLogStrategy = LogHandleStrategyEnum.find(SystemConfig.getInstance().getLogHandleStrategy());
        this.globalLogManager = globalLogStrategy.generateManager();
        this.globalLogManager.oneKeyOpen();
        this.globalLogConverter = LogDataConverterEnum.find(SystemConfig.getInstance().getLogTextType());
        this.running = true;
    }

    /**
     * 关闭服务
     *
     * @throws Exception
     */

    public synchronized void stop() throws Exception {
        if (!running) {
            return;
        }
        running = false;
        globalLogManager.oneKeyClose();
        SystemLogger.info(globalLogManager.managerName() + " to stop.");
    }

    /**
     * 向服务投递 log 消息
     *
     * @param loggerName
     * @param source
     * @param remote
     */
    public void offer(String loggerName, Object source, String remote) {
        if (!this.running) {
            SystemLogger.error("LogCenter not running .");
            return;
        }
        LogData data = null;
        try {
            data = this.globalLogConverter.convertLogData(loggerName, source, remote, System.currentTimeMillis());
            this.globalLogManager.offer(data);
        } catch (Throwable e) {
            SystemLogger.error("LogCenter >>> offer log data error. logData=" + data, e);
        }
    }

    /**
     * @param loggerName
     * @param source
     * @param remote
     */
    public void offerPlain(String loggerName, Object source, String remote) {
        if (!this.running) {
            SystemLogger.error("LogCenter not running .");
            return;
        }
        LogData data = null;
        try {
            data = LogDataConverterEnum.PLAIN_CONVERTER.convertLogData(loggerName, source, remote,
                    System.currentTimeMillis());
            this.globalLogManager.offer(data);
        } catch (Throwable e) {
            SystemLogger.error("LogCenter >>> offer plain log data error. logData=" + data, e);
        }
    }

    /**
     * @param logService
     * @param source
     * @param remote
     */
    public void offerPlain(LogService logService, Object source, String remote) {
        if (!this.running) {
            SystemLogger.error("LogCenter not running .");
            return;
        }
        LogData data = null;
        try {
            data = LogDataConverterEnum.PLAIN_CONVERTER.convertLogData(logService.loggerName(), source, remote,
                    System.currentTimeMillis());
            this.globalLogManager.offer(data);
        } catch (Throwable e) {
            SystemLogger.error("LogCenter >>> offer plain log data error. logData=" + data, e);
        }
    }

    /**
     * @param loggerName
     * @param source
     * @param remote
     */
    public void offerJson(String loggerName, String source, String remote) {
        if (!this.running) {
            SystemLogger.error("LogCenter not running .");
            return;
        }
        LogData data = null;
        try {
            data = LogDataConverterEnum.JSON_CONVERTER.convertLogData(loggerName, source, remote,
                    System.currentTimeMillis());
            this.globalLogManager.offer(data);
        } catch (Throwable e) {
            SystemLogger.error("LogCenter >>> offer json log data error. logData=" + data, e);
        }
    }

    /**
     * 透传 log service offer json
     *
     * @param logService
     * @param source
     * @param remote
     */
    public void offerJson(LogService logService, String source, String remote) {
        if (!this.running) {
            SystemLogger.error("LogCenter not running .");
            return;
        }
        LogData data = null;
        try {
            data = LogDataConverterEnum.JSON_CONVERTER.convertLogData(logService.loggerName(), source, remote,
                    System.currentTimeMillis());
            this.globalLogManager.offer(data);
        } catch (Throwable e) {
            SystemLogger.error("LogCenter >>> offer json log data error. logData=" + data, e);
        }
    }

    public LogManager getGlobalLogManager() {
        return globalLogManager;
    }

    public LogHandleStrategyEnum getGlobalLogStrategy() {
        return globalLogStrategy;
    }

    public LogDataConverterEnum getGlobalLogConverter() {
        return globalLogConverter;
    }

    /**
     * 查找 logger 对应的 logger service
     *
     * @param loggerName
     * @return
     */

    public LogService queryService(String loggerName) {
        return globalLogManager.queryService(loggerName);
    }

    /**
     * 不停服热加载,需要停止服务
     *
     * @throws Exception
     */
    public void reload() throws Exception {
        center.stop();
        SystemConfig.getInstance().reload();
        LogCenter m = new LogCenter();
        m.start();
        center = m;
    }

}
