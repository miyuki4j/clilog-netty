package com.gow.clilog.conf;

import java.io.File;

/**
 * 服务器端常量
 *
 * @author Adam
 * @date 2020/08/04
 */
public class ServerConstant {

    /**
     * TODO 正式环境 资源文件父目录
     */
    public static final String RESOURCE_ROOT_PATH =
        System.getProperty("user.dir") + File.separator + File.separator + "resources" + File.separator;
    /**
     * TODO 开发环境 资源文件父目录
     */
    public static final String DEV_RESOURCE_ROOT_PATH = System.getProperty("user.dir") + File.separator + "src"
        + File.separator + "main" + File.separator + "resources" + File.separator;

    /**
     * TODO 正式环境 配置文件名称
     */
    public static final String CONFIG_PROPERTIES = "config.properties";

    /**
     * TODO 开发环境 配置文件名称
     */
    public static final String DEV_CONFIG_PROPERTIES = "dev"+File.separator+"config.properties";
    /**
     * TODO 正式环境 log4j 配置
     */
    public static final String LOG4J2_XML = "log4j2.xml";

    /**
     * TODO 开发环境 log4j 配置
     */
    public static final String DEV_LOG4J2_XML = "dev"+File.separator+"log4j2.xml";

    /**
     * TODO 系统属性:日志路径key
     */
    public static final String ENV_LOG_PATH = "logPath";

    /**
     * TODO 系统属性:日志级别key
     */
    public static final String ENV_LOG_LEVEL = "logLevel";

    /**
     * TODO 系统属性:是否开发模式
     */
    public static final String ENV_DEV_MODE = "dev.mode";

    /**
     * TODO 系统属性:接受log集合
     */
    public static final String ENV_UPLOAD_LOG_SET = "log.name.set";

}
