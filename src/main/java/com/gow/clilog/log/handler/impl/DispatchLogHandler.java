package com.gow.clilog.log.handler.impl;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.handler.AbstractLogHandler;
import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.monitor.OnceTimePair;
import com.gow.clilog.util.DateUtil;
import com.gow.clilog.util.SystemLogger;
import org.slf4j.Logger;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public class DispatchLogHandler extends AbstractLogHandler {

    /**
     * 转发到的另一个 direct manager
     */
    protected LogManager directManager;

    protected OnceTimePair directTimePair;

    /**
     * 
     * @param logHandlerName
     * @param loggerName
     * @param tsFormat
     * @param directManager
     */

    public DispatchLogHandler(String logHandlerName, String loggerName, String tsFormat, LogManager directManager,
        OnceTimePair directTimePair) {
        super(logHandlerName, loggerName, tsFormat);
        assert directManager != null && directTimePair != null;
        this.directManager = directManager;
        this.directTimePair = directTimePair;
    }

    /**
     * 
     * @param loggerName
     * @param tsFormat
     * @param directManager
     */
    public DispatchLogHandler(String loggerName, String tsFormat, LogManager directManager,
        OnceTimePair directTimePair) {
        super(loggerName, tsFormat);
        assert directManager != null && directTimePair != null;
        this.directManager = directManager;
        this.directTimePair = directTimePair;
    }

    public DispatchLogHandler(String loggerName, LogManager directManager, OnceTimePair directTimePair) {
        super(loggerName, DateUtil.FMT_DATE_TIME_SSS);
        assert directManager != null && directTimePair != null;
        this.directManager = directManager;
        this.directTimePair = directTimePair;
    }

    public DispatchLogHandler(String loggerName, LogManager directManager) {
        super(loggerName, DateUtil.FMT_DATE_TIME_SSS);
        assert directManager != null;
        this.directManager = directManager;
    }

    @Override
    protected Logger initLogger() {
        return null;
    }

    @Override
    public void handle(LogData logData) throws Exception {
        if (logData != null && !logData.isEmpty()) {
            try {
                if (directTimePair == null || directTimePair.getDelay() <= 0) {
                    directManager.offer(logData);
                } else {
                    directManager.offer(logData, directTimePair.getDelay(), directTimePair.getUnit());
                }
            } catch (InterruptedException ie) {
                SystemLogger.error(loggerName + " >>> handle interrupted. ", ie);
                throw ie;
            } catch (Throwable e) {
                SystemLogger.error(loggerName + " >>> handle error. ", e);
                throw e;
            }
        }

    }
}
