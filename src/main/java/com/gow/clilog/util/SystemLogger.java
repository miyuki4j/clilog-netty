package com.gow.clilog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhouhe
 * @date 2021/9/22
 */
public class SystemLogger {
    static Logger logger = LoggerFactory.getLogger("SystemLogger");

    public static void trace(String msg) {
        logger.trace(msg);
    }

    public static void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    public static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public static void debug(String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg, t);
        }
    }

    public static void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public static void info(String msg, Throwable t) {
        if (logger.isInfoEnabled()) {
            logger.info(msg, t);
        }
    }

    public static void error(String s, Throwable e) {
        if (logger.isErrorEnabled()) {
            logger.error(s, e);
        }
    }

    public static void error(String s) {
        if (logger.isErrorEnabled()) {
            logger.error(s);
        }
    }

    public static void error(String message, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(message, params);
        }
    }
}
