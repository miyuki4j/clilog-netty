package com.gow.clilog.log.type;

import com.gow.clilog.log.manager.LogManager;
import com.gow.clilog.log.manager.impl.DirectLogManager;
import com.gow.clilog.log.manager.impl.DispatchLogManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouhe
 * @date 2021/9/28
 */
public enum LogHandleStrategyEnum {

    /** DIRECT("direct", "直接处理模式") */
    DIRECT("direct", "直接处理模式") {
        @Override
        public LogManager generateManager() throws Exception {
            return new DirectLogManager();
        }
    },

    /** DISPATCH("dispatch", "转发处理模式") */
    DISPATCH("dispatch", "转发处理模式") {
        @Override
        public LogManager generateManager() throws Exception {
            return new DispatchLogManager();
        }
    },

    ;

    static Map<String, LogHandleStrategyEnum> map = new HashMap<>();

    static {
        init();
    }

    public final String strategyName;
    public final String desc;

    LogHandleStrategyEnum(String strategyName, String desc) {
        this.strategyName = strategyName;
        this.desc = desc;
    }

    static void init() {
        for (LogHandleStrategyEnum value : values()) {
            map.put(value.strategyName, value);
        }
    }

    /**
     * 查找处理策略
     * 
     * @param strategyName
     * @return
     */
    public static LogHandleStrategyEnum find(String strategyName) throws Exception {
        return map.get(strategyName);
    }

    /**
     * 检查处理策略
     * 
     * @param strategyName
     * @return
     */
    public static String check(String strategyName) throws Exception {
        if (strategyName == null || strategyName.isEmpty()) {
            throw new NullPointerException("LogHandleStrategyEnum >>> check strategyName is null.");
        }
        if (!map.containsKey(strategyName)) {
            throw new NullPointerException(
                "LogHandleStrategyEnum >>> check strategyName not exists. cur strategy name=" + strategyName);
        }
        return strategyName;
    }

    /**
     * 构造对应策略的 log manager
     * 
     * @return
     * @throws Exception
     */
    public abstract LogManager generateManager() throws Exception;
}
