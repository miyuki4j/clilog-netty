package com.gow.clilog.log.handler.impl;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.handler.AbstractLogHandler;
import com.gow.clilog.util.DateUtil;
import com.gow.clilog.util.SystemLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhouhe
 * @date 2021/9/27
 */
public class DefaultLogHandler extends AbstractLogHandler {

    public DefaultLogHandler(String logHandlerName, String loggerName, String tsFormat) {
        super(logHandlerName, loggerName, tsFormat);
    }

    public DefaultLogHandler(String loggerName, String tsFormat) {
        super(loggerName, tsFormat);
    }

    public DefaultLogHandler(String loggerName) {
        super(loggerName, DateUtil.FMT_DATE_TIME_SSS);
    }

    @Override
    public void handle(LogData logData) throws Exception {
        if (logData != null && !logData.isEmpty()) {
            String dateStr;
            try {
                SimpleDateFormat format = getDateFormat();
                dateStr = format.format(new Date(logData.createTime()));
            } catch (Throwable e) {
                dateStr = "";
            }
            try {
                String text = logData.toLogText(dateStr);
                logger.info(text);
            } catch (Throwable e) {
                SystemLogger.error(loggerName + " >>> handle error. ", e);
            }
        }
    }

}