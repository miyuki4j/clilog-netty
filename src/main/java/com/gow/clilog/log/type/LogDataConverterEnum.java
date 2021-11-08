package com.gow.clilog.log.type;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.data.impl.JsonLogData;
import com.gow.clilog.log.data.impl.PlainLogData;
import com.gow.clilog.conf.SystemConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouhe
 * @date 2021/9/27
 */
@SuppressWarnings("all")
public enum LogDataConverterEnum {

    /** PLAIN_CONVERTER(1, "原始字符串Log Data 转换器") */
    PLAIN_CONVERTER("plain", "原始字符串Log Data 转换器") {
        @Override
        public LogData convertLogData(String logger, Object source, String remote, long comeTime, Object... extParam)
            throws Exception {
            if (source == null || !(source instanceof String)) {
                throw new RuntimeException("source is not string. " + source);
            }
            String s = (String)source;
            return new PlainLogData(comeTime, s).remoteAddress(remote).loggerName(logger);
        }
    },

    /** JSON_CONVERTER(2, "json字符串Log Data 转换器") */
    JSON_CONVERTER("json", "json字符串Log Data 转换器") {
        @Override
        public LogData convertLogData(String logger, Object source, String remote, long comeTime, Object... extParam)
            throws Exception {
            if (source == null || !(source instanceof String)) {
                throw new RuntimeException("source is not string. " + source);
            }
            String s = (String)source;
            return new JsonLogData(comeTime, s).remoteAddress(remote).loggerName(logger);
        }
    },

    ;

    static Map<String, LogDataConverterEnum> map = new HashMap<>();

    static {
        initMap();
    }

    public final String typeName;
    public final String desc;

    LogDataConverterEnum(String typeName, String desc) {
        this.typeName = typeName;
        this.desc = desc;
    }

    static void initMap() {
        for (LogDataConverterEnum value : values()) {
            map.put(value.typeName, value);
        }
    }

    /**
     *
     * @param type
     * @param source
     * @param remote
     * @param comeTime
     * @param extParam
     * @return
     * @throws Exception
     */
    public static final LogData convert(String typeName, String logger, Object source, String remote, long comeTime,
                                        Object... extParam) throws Exception {
        LogDataConverterEnum converterEnum = map.get(typeName);
        if (converterEnum == null) {
            converterEnum = PLAIN_CONVERTER;
        }
        return converterEnum.convertLogData(logger, source, remote, comeTime, extParam);
    }

    /**
     *
     * @param type
     * @param logger
     * @param source
     * @param remote
     * @param extParam
     * @return
     * @throws Exception
     */
    public static final LogData convert(String logger, Object source, String remote, Object... extParam)
        throws Exception {
        String textType = SystemConfig.getInstance().getLogTextType();
        return convert(textType, logger, source, remote, System.currentTimeMillis(), extParam);
    }

    /**
     * 检查 log text type
     * 
     * @param typeName
     * @return
     * @throws Exception
     */
    public static String check(String typeName) throws Exception {
        if (typeName == null || typeName.isEmpty()) {
            throw new NullPointerException("LogDataConverterEnum >>> check typeName is null.");
        }
        if (!map.containsKey(typeName)) {
            throw new NullPointerException(
                "LogDataConverterEnum >>> check typeName not exists. cur type name =" + typeName);
        }
        return typeName;
    }

    /**
     * 查找转换器
     * 
     * @param typeName
     * @return
     */
    public static LogDataConverterEnum find(String typeName) {
        return map.get(typeName);
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * @param logger
     *            所属logger
     * @param source
     *            数据源
     * @param remote
     * @param comeTime
     * @param extParam
     * @return
     * @throws Exception
     */
    public abstract LogData convertLogData(String logger, Object source, String remote, long comeTime,
                                           Object... extParam) throws Exception;
}
