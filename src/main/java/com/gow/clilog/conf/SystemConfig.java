package com.gow.clilog.conf;

import java.util.LinkedHashSet;
import java.util.Set;

import com.gow.clilog.log.type.LogDataConverterEnum;
import com.gow.clilog.log.type.LogHandleStrategyEnum;
import com.gow.clilog.netty.type.NettyServerEnum;
import com.gow.clilog.util.PropertiesConfig;

/**
 * 系统配置
 *
 * @author Adam
 * @date 2020/06/04
 */
@SuppressWarnings("all")
public class SystemConfig extends PropertiesConfig {

    /**
     * logger热加载检测, 默认10分钟检测一次
     */
    public static int DEFAULT_RELOAD_CHECK_GAP_SECOND = 60 * 10;

    /**
     * logger queue state check, 默认3分钟检测一次
     */
    public static int DEFAULT_QUEUE_CHECK_GAP_SECOND = 60 * 3;

    /**
     * 默认的输出logger
     */
    public static String DEFAULT_OUTPUT_LOGGER_NAME = "OTHER";

    private static SystemConfig instance = new SystemConfig();

    private String projectId;// 项目ID

    private boolean devMode;// 是否开发模式

    private String logPath;// 日志路径

    private boolean useEntity = false;// 是否使用实体模块

    private Set<String> logNameSet = new LinkedHashSet<>(); // 日志名称集合

    private long logReloadGap = DEFAULT_RELOAD_CHECK_GAP_SECOND; // 日志热加载检测间隔

    private long logQueueGap = DEFAULT_QUEUE_CHECK_GAP_SECOND; // 日志队列检测间隔

    private String logDefaultName = DEFAULT_OUTPUT_LOGGER_NAME; // 默认的日志，找不到时默认输出logger

    private String logTextType = DEFAULT_OUTPUT_LOGGER_NAME; // log数据格式，plain:普通字符串，json:json字符串

    private String logHandleStrategy = DEFAULT_OUTPUT_LOGGER_NAME; // log处理策略 direct:直接处理模式,dispatch:转发模式

    private boolean logServicePlaceServlet; // servlet 初始化的时候是否加载log service 加速log data 的处理

    private String nettyServerType;

    private String nettyServerName;

    private int nettyServerPort;

    private int rmiPort;

    private int rmiServerPort;

    private SystemConfig() {

    }

    public static SystemConfig getInstance() {
        return instance;
    }

    /**
     * 获得项目ID
     *
     * @return the projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * 是否开发模式
     *
     * @return the devMode
     */
    public boolean isDevMode() {
        return devMode;
    }

    /**
     * 获得日志路径
     *
     * @return the logPath
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * 是否使用实体模块
     *
     * @return the useEntity
     */
    public boolean isUseEntity() {
        return useEntity;
    }

    public Set<String> getLogNameSet() {
        return logNameSet;
    }

    public long getLogReloadGap() {
        return logReloadGap;
    }

    public long getLogQueueGap() {
        return logQueueGap;
    }

    public String getLogDefaultName() {
        return logDefaultName;
    }

    public String getLogTextType() {
        return logTextType;
    }

    public String getLogHandleStrategy() {
        return logHandleStrategy;
    }

    public boolean isLogServicePlaceServlet() {
        return logServicePlaceServlet;
    }

    /**
     * 日志级别
     *
     * @return
     */
    public String getLogLevel() {
        return isDevMode() ? "DEBUG" : "INFO";
    }

    public String getNettyServerType() {
        return nettyServerType;
    }

    public int getNettyServerPort() {
        return nettyServerPort;
    }

    /**
     * 开发模式
     *
     * @return
     */
    public String getMode() {
        return Boolean.valueOf(isDevMode()).toString();
    }

    public String getNettyServerName() {
        return nettyServerName;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public int getRmiServerPort() {
        return rmiServerPort;
    }

    /* (non-Javadoc)
     * @see com.gow.common.util.PropertiesConfig#loadProperties()
     */
    @Override
    protected void loadProperties() throws Exception {
        this.projectId = prop.getProperty("project.id", "").trim();
        this.devMode = Boolean.parseBoolean(prop.getProperty("dev.mode").trim());
        this.logPath = prop.getProperty("log.path").trim();
        if (prop.containsKey("em.use")) {
            this.useEntity = Boolean.parseBoolean(prop.getProperty("em.use").trim());
        }
        String logNameSetStr = prop.getProperty("log.name.set");
        if (logNameSetStr != null && !(logNameSetStr = logNameSetStr.trim()).isEmpty()) {
            for (String logName : logNameSetStr.split(";")) {
                this.logNameSet.add(logName);
            }
        }
        this.logReloadGap = Long.parseLong(prop.getProperty("log.reload.gap", "600").trim());
        this.logQueueGap = Long.parseLong(prop.getProperty("log.queue.gap", "180").trim());
        this.logDefaultName = prop.getProperty("log.default.name", "OTHER").trim();
        this.logTextType = LogDataConverterEnum.check(prop.getProperty("log.text.type"));
        this.logHandleStrategy = LogHandleStrategyEnum.check(prop.getProperty("log.handle.strategy"));
        this.logServicePlaceServlet =
            Boolean.parseBoolean(prop.getProperty("log.service.place.servlet", "true").trim());
        this.nettyServerType = NettyServerEnum.check(prop.getProperty("netty.server.type", "nio"));
        this.nettyServerName = prop.getProperty("netty.server.name").trim();
        this.nettyServerPort = Integer.parseInt(prop.getProperty("netty.server.port", "8080"));
        this.rmiPort = Integer.parseInt(prop.getProperty("rmi.port", "8098"));
        this.rmiServerPort = Integer.parseInt(prop.getProperty("rmi.server.port", "8099"));
    }

    /**
     * 热加载
     *
     * @throws Exception
     */
    public void reload() throws Exception {
        SystemConfig m = new SystemConfig();
        m.load(instance.filePath);
        SystemConfig old = instance;
        instance = m;
        old.destory();
    }

    /**
     * 获取一个实时的配置
     *
     * @return
     * @throws Exception
     */
    public SystemConfig realTimeConfig() throws Exception {
        SystemConfig m = new SystemConfig();
        m.load(instance.filePath);
        return m;
    }
}
