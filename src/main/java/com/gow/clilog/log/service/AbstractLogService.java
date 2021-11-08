package com.gow.clilog.log.service;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.handler.LogHandler;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.OnceTimePair;
import com.gow.clilog.log.queue.LogQueue;
import com.gow.clilog.util.SystemLogger;
import com.gow.clilog.util.ThreadHelper;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/27
 */
public abstract class AbstractLogService implements LogService {

    /** 服务处于新生状态 */
    public static int SERVICE_STATE_VIRGIN = 0;

    /** 服务处于初始化状态 */
    public static int SERVICE_STATE_INITED = 1;

    /** 服务处于运行状态 */
    public static int SERVICE_STATE_RUNNING = 2;

    /** 服务处于正在停止状态 */
    public static int SERVICE_STATE_STOPING = 4;

    /** 服务处于已停止状态 */
    public static int SERVICE_STATE_STOPED = 8;

    /** 服务处于终结状态 */
    public static int SERVICE_STATE_TERMINATED = 16;

    /** log服务的名称,默认 logger name + service suffix */
    protected final String serviceName;

    /** logger name */
    protected final String loggerName;

    /** log queue */
    protected LogQueue logQueue;

    /** log handler */
    protected LogHandler logHandler;

    /** log queue poll 的超时等待参数 */
    protected OnceTimePair pollTimePair;

    /** 处理log的服务线程 */
    protected Thread logServiceThread;

    /** 所属的parent */
    protected LogManager parent;

    /** 服务状态 */
    protected volatile int state = SERVICE_STATE_VIRGIN;

    /**
     * 
     * @param loggerName
     * @param serviceName
     * @param logQueue
     * @param logHandler
     * @param pollTimePair
     */

    protected AbstractLogService(String loggerName, String serviceName, LogQueue logQueue, LogHandler logHandler,
                                 OnceTimePair pollTimePair) {
        assert loggerName != null;
        this.loggerName = loggerName;
        this.serviceName = serviceName;
        this.logQueue = logQueue;
        this.logHandler = logHandler;
        this.pollTimePair = pollTimePair;
        this.logQueue.parent(this);
        this.logHandler.parent(this);
        onConstructor();
    }

    /**
     *
     * @param loggerName
     * @param logQueue
     * @param logHandler
     * @param pollTimePair
     */
    protected AbstractLogService(String loggerName, LogQueue logQueue, LogHandler logHandler,
                                 OnceTimePair pollTimePair) {
        this(loggerName, loggerName + SERVICE_NAME_SUFFIX, logQueue, logHandler, pollTimePair);
    }

    @Override
    public synchronized void init() throws Exception {
        if (state != SERVICE_STATE_VIRGIN && state != SERVICE_STATE_TERMINATED) {
            throw new IllegalStateException(
                "[" + serviceName + "] cur state not is SERVICE_STATE_VIRGIN, the state=" + state);
        }
        this.logQueue.init();
        this.logHandler.init();
        initOtherParam();
        switchState(SERVICE_STATE_INITED);
    }

    @Override
    public synchronized void start() throws Exception {
        switchState(SERVICE_STATE_INITED, SERVICE_STATE_RUNNING);
        this.logServiceThread = doStartThread();
    }

    @Override
    public void run() {
        try {
            runLoop(Thread.currentThread());
        } catch (Throwable e) {
            // skip
        } finally {
            try {
                onRan(Thread.currentThread());
            } catch (Throwable e) {
                // skip
            }
        }
    }

    @Override
    public synchronized void stop() throws Exception {
        switchState(SERVICE_STATE_RUNNING, SERVICE_STATE_STOPING);
        doJoinShutdown(this.logServiceThread);
        switchState(SERVICE_STATE_STOPING, SERVICE_STATE_STOPED);
    }

    @Override
    public synchronized void destroy() throws Exception {
        doDestory();
        unParent();
        switchState(SERVICE_STATE_STOPED, SERVICE_STATE_TERMINATED);
    }

    /**
     * 启动线程
     * 
     * @return
     */
    protected Thread doStartThread() {
        Thread t = new ThreadHelper(this, serviceName);
        t.setDaemon(true);
        t.start();
        return t;
    }

    /**
     * 停止线程
     * 
     * @param thread
     */
    protected void doJoinShutdown(Thread thread) {
        if (thread == null) {
            return;
        }
        if (thread instanceof ThreadHelper) {
            ((ThreadHelper)thread).interruptShutdown();
        } else {
            thread.interrupt();
            while (true) {
                try {
                    thread.join();
                    break;
                } catch (Throwable e) {
                }
            }
        }
    }

    /**
     * 停止之后的一些销毁动作
     */
    protected void doDestory() throws Exception {
        logQueue.destory();
        logHandler.destory();
        logQueue = null;
        logHandler = null;
        logServiceThread = null;
    }

    ////////////////////////////////////////// state method //////////////////////////////////////////

    /**
     * 切换状态
     * 
     * @param expected
     * @param update
     * @throws IllegalStateException
     */
    protected synchronized void switchState(int expected, int update) throws IllegalStateException {
        if (state != expected) {
            throw new IllegalStateException(
                "[" + serviceName + "] previous state not is SERVICE_STATE_INITED, the previous state=" + state);
        }
        state = update;
    }

    /**
     * 不断言
     * 
     * @param update
     */
    protected synchronized void switchState(int update) {
        state = update;
    }

    @Override
    public boolean isVirgin() {
        return state == SERVICE_STATE_VIRGIN;
    }

    @Override
    public boolean isInited() {
        return state >= SERVICE_STATE_INITED;
    }

    @Override
    public boolean isRunning() {
        return state == SERVICE_STATE_RUNNING;
    }

    @Override
    public boolean isStoping() {
        return state == SERVICE_STATE_STOPING;
    }

    @Override
    public boolean isStoped() {
        return state >= SERVICE_STATE_STOPED;
    }

    @Override
    public boolean isTerminated() {
        return state >= SERVICE_STATE_TERMINATED;
    }

    @Override
    public String serviceState() {
        if (logQueue == null || logHandler == null) {
            return "";
        }
        return loggerName() + " " + logQueue.queueState() + " " + logHandler.handlerState();
    }

    @Override
    public String serviceName() {
        return serviceName;
    }

    @Override
    public String loggerName() {
        return loggerName;
    }

    @Override
    public LogQueue logQueue() {
        return logQueue;
    }

    @Override
    public LogHandler logHandler() {
        return logHandler;
    }

    @Override
    public LogManager parent() {
        return parent;
    }

    @Override
    public void parent(Parentable p) {
        parent((LogManager)p);
    }

    @Override
    public void parent(LogManager parent) {
        if (parent == null) {
            throw new NullPointerException(serviceName + " >>> service set parent(manager) is null.");
        }
        this.parent = parent;
    }

    @Override
    public void unParent() {
        this.parent = null;
    }

    @Override
    public boolean offer(LogData logData) throws Exception {
        if (logData == null || logData.source() == null || !isRunning()) {
            return false;
        }
        return logQueue.offer(logData);
    }

    @Override
    public boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception {
        if (logData == null || logData.source() == null || !isRunning()) {
            return false;
        }
        return logQueue.offer(logData, timeout, unit);
    }

    // ***************************************** sub class implements ***************************************** //

    /**
     * 子类的处理循环
     *
     * @param curThread
     *            runLoop 所在的线程
     * @throws Exception
     */
    protected abstract void runLoop(final Thread curThread) throws Exception;

    /**
     * 初始化一些其他参数, 比如 queue#poll 的时间,根据子类的需要决定
     *
     * @throws Exception
     */
    protected void initOtherParam() throws Exception {
        SystemLogger.info(serviceName + " initOtherParam");
    }

    /**
     * 构造函数的结束工作,子类根据需要 override
     */
    protected void onConstructor() {
        SystemLogger.info(serviceName + " onConstructor.");
    }

    /**
     * 跑完之后的一些操作
     *
     * @param curThread
     *            onRan 所在的线程
     */
    protected void onRan(final Thread curThread) {
        SystemLogger.info(curThread.getName() + " stop.");
    }

}
