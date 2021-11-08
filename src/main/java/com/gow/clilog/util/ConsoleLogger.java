package com.gow.clilog.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 控制台日志
 * 
 * @author Adam
 * @date 2020/08/06
 */
public class ConsoleLogger {

    public static void debug(String message) {
        printlnMessage(LEVEL_DEBUG, message);
    }

    public static void info(String message) {
        printlnMessage(LEVEL_INFO, message);
    }

    public static void warn(String message) {
        printlnMessage(LEVEL_WARN, message);
    }

    public static void error(String message) {
        error(message, null);
    }

    public static void error(String message, Throwable cause) {
        printlnMessage(LEVEL_ERROR, message);
        if (null != cause) {
            cause.printStackTrace();
        }
    }

    private static void printlnMessage(int level, String message) {
        PrintStream ps = getPrinter(level);
        StringBuilder toSb = new StringBuilder();
        toSb.append(getDateTime()).append(" ").append(getLevelMark(level)).append(" ").append(message);
        ps.println(toSb.toString());
        ps.flush();
    }

    /**
     * 根据日志等级获得打印器
     * 
     * @param level
     * @return
     */
    private static PrintStream getPrinter(int level) {
        if (LEVEL_DEBUG == level || LEVEL_INFO == level) {
            return out;
        }
        return err;
    }

    /**
     * 获得日志等级标记
     * 
     * @param level
     * @return
     */
    private static String getLevelMark(int level) {
        if (LEVEL_DEBUG == level) {
            return MARK_DEBUG;
        }
        if (LEVEL_INFO == level) {
            return MARK_INFO;
        }
        if (LEVEL_WARN == level) {
            return MARK_WARN;
        }
        if (LEVEL_ERROR == level) {
            return MARK_ERROR;
        }
        if (LEVEL_FATAL == level) {
            return MARK_FATAL;
        }
        return MARK_DEBUG;
    }

    /**
     * 获得当前日期时间字符串 <br>
     * 格式: yyyy-MM-dd HH:mm:ss.SSS
     * 
     * @return
     */
    private static synchronized String getDateTime() {
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    private static final PrintStream out = System.out;

    private static final PrintStream err = System.err;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final int LEVEL_DEBUG = 0;
    private static final int LEVEL_INFO = 1;
    private static final int LEVEL_WARN = 2;
    private static final int LEVEL_ERROR = 3;
    private static final int LEVEL_FATAL = 4;

    private static final String MARK_DEBUG = "DEBUG";
    private static final String MARK_INFO = "INFO ";
    private static final String MARK_WARN = "WARN ";
    private static final String MARK_ERROR = "ERROR";
    private static final String MARK_FATAL = "FATAL";

}
