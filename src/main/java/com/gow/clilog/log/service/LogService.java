package com.gow.clilog.log.service;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.handler.LogHandler;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.queue.LogQueue;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @description: integretation of log service ,follow the form of (per-logServiceThread,per-blockingqueue,per-logger)
 */
public interface LogService extends Runnable, Parentable {

    String SERVICE_NAME_SUFFIX = "-LogService";

    /**
     * 服务名称
     * 
     * @return
     */
    String serviceName();

    String loggerName();

    /**
     * log 队列
     * 
     * @return
     */
    LogQueue logQueue();

    /**
     * log 处理器
     * 
     * @return
     */
    LogHandler logHandler();

    /**
     * 所属
     * 
     * @return
     */
    LogManager parent();

    /**
     * 设置所属
     * 
     * @param parent
     */
    void parent(LogManager parent);

    /**
     * stop之前的一些初始化工作
     * 
     * @return
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 在init之后启动
     * 
     * @return
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * destory 之前执行
     *
     * @return
     * @throws Exception
     */
    void stop() throws Exception;

    /**
     * stop 之后的一些释放工作
     * 
     * @throws Exception
     */
    void destroy() throws Exception;

    /** -----------------------服务的状态 start --------------------------- */
    boolean isVirgin();

    boolean isInited();

    boolean isRunning();

    boolean isStoping();

    boolean isStoped();

    boolean isTerminated();

    String serviceState();

    /** -----------------------服务的状态 end--------------------------- */

    /** -----------------------operation--------------------------- */

    /**
     * 向log service 投递一个 log data
     * 
     * @param logData
     * @return true:成功, false:失败
     * @throws Exception
     */
    boolean offer(LogData logData) throws Exception;

    /**
     * 向log service 投递一个 log data
     * 
     * @param logData
     * @param timeout
     * @param unit
     * @return
     * @throws Exception
     */
    boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception;

}
