package com.gow.clilog.conf;

import java.io.File;
import java.net.URI;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

import com.gow.clilog.main.MainServer;
import com.gow.clilog.util.ConsoleLogger;
import com.gow.clilog.util.SystemLogger;
import com.gow.clilog.util.SystemUtil;

public final class ResourcesManager {

    private static ResourcesManager instance = new ResourcesManager();
    private int PID = -1;
    /**
     * 日志上下文
     */
    private LoggerContext log4j2Context;
    /**
     * 是否开发环境
     */
    private boolean IDE;
    /**
     * TODO 当前项目根路径
     */
    private String rootPath;
    /**
     * TODO 当前项目资源根目录
     */
    private String resourcesRootPath;
    /**
     * TODO 配置文件的路径
     */
    private String configPropPath;
    /**
     * log4j 配置文件的路径
     */
    private String log4j2XmlPath;

    public static ResourcesManager getInstance() {
        return instance;
    }

    public int getPID() {
        return MainServer.getPID();
    }

    public LoggerContext getLog4j2Context() {
        return log4j2Context;
    }

    public void setLog4j2Context(LoggerContext log4j2Context) {
        this.log4j2Context = log4j2Context;
    }

    public boolean isIDE() {
        return IDE;
    }

    public void setIDE(boolean IDE) {
        this.IDE = IDE;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getResourcesRootPath() {
        return resourcesRootPath;
    }

    public void setResourcesRootPath(String resourcesRootPath) {
        this.resourcesRootPath = resourcesRootPath;
    }

    public String getConfigPropPath() {
        return configPropPath;
    }

    public void setConfigPropPath(String configPropPath) {
        this.configPropPath = configPropPath;
    }

    public String getLog4j2XmlPath() {
        return log4j2XmlPath;
    }

    public void setLog4j2XmlPath(String log4j2XmlPath) {
        this.log4j2XmlPath = log4j2XmlPath;
    }

    /**
     * 开发环境
     *
     * @throws Exception
     */
    private void loadIDEProp() throws Exception {
        setRootPath(System.getProperty("user.dir") + File.separator);
        setResourcesRootPath(ServerConstant.DEV_RESOURCE_ROOT_PATH);
        setLog4j2XmlPath(getResourcesRootPath() + ServerConstant.DEV_LOG4J2_XML);
        setConfigPropPath(getResourcesRootPath() + ServerConstant.DEV_CONFIG_PROPERTIES);
    }

    /**
     * 正式环境
     *
     * @throws Exception
     */
    private void loadProp() throws Exception {
        setRootPath(System.getProperty("user.dir") + File.separator);
        setResourcesRootPath(ServerConstant.RESOURCE_ROOT_PATH);
        setLog4j2XmlPath(getResourcesRootPath() + ServerConstant.LOG4J2_XML);
        setConfigPropPath(getResourcesRootPath() + ServerConstant.CONFIG_PROPERTIES);
    }

    /**
     * 加载系统属性
     *
     * @throws Exception
     */
    private void loadSysProp() throws Exception {
        setIDE(SystemUtil.inIDE());
        if (isIDE()) {
            loadIDEProp();
        } else {
            loadProp();
        }
        File file = new File(getConfigPropPath());
        if (!file.exists()) {
            throw new AssertionError(getConfigPropPath() + " not exists.");
        }
        SystemConfig.getInstance().load(file);

        // 设置日志目录
        System.setProperty(ServerConstant.ENV_LOG_PATH, SystemConfig.getInstance().getLogPath());
        // 设置日志级别
        System.setProperty(ServerConstant.ENV_LOG_LEVEL, SystemConfig.getInstance().getLogLevel());
        // 设置是否开发模式
        System.setProperty(ServerConstant.ENV_DEV_MODE, SystemConfig.getInstance().getMode());
    }

    /**
     * 加载 log4j
     *
     * @throws Exception
     */
    private void loadLog4j2() throws Exception {
        String log4j2Xml = getLog4j2XmlPath();
        this.log4j2Context = Configurator.initialize(null, log4j2Xml);
    }

    /**
     * 属性 配置 加载
     *
     * @throws Exception
     */
    public void load() throws Exception {
        ConsoleLogger.info("Clilog[" + getPID() + "] ResourcesManager initializing start...");
        try {
            loadSysProp();
            loadLog4j2();
        } catch (Throwable e) {
            ConsoleLogger.info("Clilog[" + getPID() + "] ResourcesManager initializing error. " + e);
            e.printStackTrace();
            throw e;
        }
        SystemLogger.info("Clilog[" + getPID() + "] ResourcesManager initializing end...");
    }

    /**
     * 卸载资源
     *
     * @throws Exception
     */
    public void unload() throws Exception {
        if (this.log4j2Context != null) {
            Configurator.shutdown(this.log4j2Context);
            this.log4j2Context = null;
            ConsoleLogger.info("ResourcesManager unload.");
        }
    }

    /**
     * 热加载
     *
     * @throws Exception
     */
    public void reload() throws Exception {

    }

}
