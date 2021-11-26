package com.gow.clilog.log.factory;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.handler.LogHandler;
import com.gow.clilog.log.handler.impl.DefaultLogHandler;
import com.gow.clilog.log.handler.impl.DispatchLogHandler;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.OnceTimePair;
import com.gow.clilog.log.queue.LogQueue;
import com.gow.clilog.log.queue.impl.DefaultLogQueue;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.log.service.impl.DefaultLogService;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/28
 * @description: log 服务工厂
 */
public final class LogServiceFactory {
    /**
     * create a log service of directed-mode
     * 
     * @param loggerName
     * @param parentManager
     * @param pollTimePair
     * @return
     */
    public static LogService createDirectService(String loggerName, LogManager parentManager, OnceTimePair pollTimePair)
        throws Exception {
        LogQueue logQueue = new DefaultLogQueue(loggerName, PlatformDependent.<LogData>newMpscQueue());
        LogHandler logHandler = new DefaultLogHandler(loggerName);
        LogService service = new DefaultLogService(loggerName, logQueue, logHandler, pollTimePair);
        service.parent(parentManager);
        service.init();
        return service;
    }

    /**
     * create a log service of directed-mode
     * 
     * @param loggerName
     * @param parentManager
     * @return
     */
    public static LogService createDirectService(String loggerName, LogManager parentManager) throws Exception {
        return createDirectService(loggerName, parentManager, new OnceTimePair(3L, TimeUnit.SECONDS));
    }

    /**
     * 创建一个转发 log manager
     * 
     * @param parentLogManager
     * @param parentPollTimePair
     * @param directManager
     * @return
     * @throws Exception
     */
    public static LogService createDispatchService(String loggerName, String serviceName, LogManager parentLogManager,
        OnceTimePair parentPollTimePair, LogManager directManager) throws Exception {
        LogQueue logQueue = new DefaultLogQueue(loggerName, PlatformDependent.<LogData>newSpscQueue());
        LogHandler logHandler = new DispatchLogHandler(serviceName, directManager);
        LogService service = new DefaultLogService(loggerName, serviceName, logQueue, logHandler, parentPollTimePair);
        service.parent(parentLogManager);
        service.init();
        return service;
    }

    /**
     * 创建一个转发log服务
     * 
     * @param parentLogManager
     * @param directManager
     * @return
     * @throws Exception
     */
    public static LogService createDispatchService(String loggerName, String serviceName, LogManager parentLogManager,
        LogManager directManager) throws Exception {
        return createDispatchService(loggerName, serviceName, parentLogManager, new OnceTimePair(3L, TimeUnit.SECONDS),
            directManager);
    }

    /**
     * 构造一个 dispatch to direct log service
     * 
     * @param loggerName
     * @param parentManager
     * @param pollTimePair
     * @return
     * @throws Exception
     */
    public static LogService createDispatch2DirectService(String loggerName, LogManager parentManager,
        OnceTimePair pollTimePair) throws Exception {
        LogQueue logQueue =
            new DefaultLogQueue(loggerName, new PriorityBlockingQueue<com.gow.clilog.log.data.LogData>());
        LogHandler logHandler = new DefaultLogHandler(loggerName);
        LogService service = new DefaultLogService(loggerName, logQueue, logHandler, pollTimePair);
        service.parent(parentManager);
        service.init();
        return service;
    }
}
