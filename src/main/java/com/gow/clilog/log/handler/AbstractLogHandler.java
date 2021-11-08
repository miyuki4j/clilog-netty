package com.gow.clilog.log.handler;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.util.SystemLogger;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhouhe
 * @date 2021/9/27
 */
public abstract class AbstractLogHandler implements LogHandler {
    public static String HANDLER_NAME_SUFFIX = "-LogHandler";

    protected final String logHandlerName;

    protected final String loggerName;

    protected String dateFormat;

    /** key: tid, value:SimpleDateFormat */
    protected ConcurrentMap<Long, SimpleDateFormat> dateFormatMap = new ConcurrentHashMap<>();

    protected org.slf4j.Logger logger;

    protected LogService parent;

    // 处理计数
    protected AtomicLong handleCounter = new AtomicLong();
    protected AtomicLong handleCounterSucc = new AtomicLong();

    protected AbstractLogHandler(String logHandlerName, String loggerName, String tsFormat) {
        this.logHandlerName = logHandlerName;
        this.loggerName = loggerName;
        this.dateFormat = tsFormat;
    }

    protected AbstractLogHandler(String loggerName, String tsFormat) {
        this(loggerName + HANDLER_NAME_SUFFIX, loggerName, tsFormat);
    }

    @Override
    public LogService parent() {
        return parent;
    }

    @Override
    public void parent(LogService parent) {
        if (parent == null) {
            throw new NullPointerException(handlerName() + " >>> handler set parent(service) is null.");
        }
        this.parent = parent;
    }

    @Override
    public void parent(Parentable p) {
        parent((LogService)p);
    }

    @Override
    public void unParent() {
        this.parent = null;
    }

    @Override
    public String handlerName() {
        return logHandlerName;
    }

    @Override
    public String loggerName() {
        return loggerName;
    }

    @Override
    public void init() throws Exception {
        this.logger = initLogger();
        onInit();
    }

    /**
     * 留给子类的钩子
     * 
     * @throws Exception
     */
    protected void onInit() throws Exception {
        SystemLogger.info(logHandlerName + " >>> onInit.");
    }

    @Override
    public void destory() throws Exception {
        onDestory();
        dateFormatMap.clear();
        dateFormatMap = null;
        logger = null;
        unParent();
    }

    /**
     * 留给子类的钩子
     * 
     * @throws Exception
     */
    protected void onDestory() throws Exception {
        SystemLogger.info(logHandlerName + " >>> onDestory.");
    }

    /**
     * 初始化一个logger,子类可以根据需要修改, 决定如何获取
     * 
     * @return
     * @throws Exception
     */
    protected org.slf4j.Logger initLogger() {
        return org.slf4j.LoggerFactory.getLogger(loggerName);
    }

    /**
     * 默认获取SimpleDateFormat的方式, 子类可以根据需要决定如何获取
     * 
     * @return
     */
    protected SimpleDateFormat getDateFormat() {
        long id = Thread.currentThread().getId();
        SimpleDateFormat format = dateFormatMap.get(id);
        if (format != null) {
            return format;
        }
        format = new SimpleDateFormat(dateFormat);
        dateFormatMap.put(id, format);
        return format;
    }

    @Override
    public long incHandleCnt() {
        return handleCounter.incrementAndGet();
    }

    @Override
    public long incHandleSuccessCnt() {
        return handleCounterSucc.incrementAndGet();
    }

    @Override
    public String handlerState() {
        return String.format("handleCounter=[%d], handleCounterSucc=[%d]", handleCounter.get(),
            handleCounterSucc.get());
    }
}
