package com.gow.clilog.main;

import com.gow.clilog.conf.ResourcesManager;
import com.gow.clilog.conf.SystemConfig;
import com.gow.clilog.log.LogCenter;
import com.gow.clilog.mbean.JMXServer;
import com.gow.clilog.mbean.MBeans;
import com.gow.clilog.netty.GlobalNettyServer;
import com.gow.clilog.util.ConsoleLogger;
import com.gow.clilog.util.SystemLogger;
import com.gow.clilog.util.SystemUtil;

public class MainServer {
    private static int PID = -1;
    private static JMXServer jmxServer;

    public static int getPID() {
        return PID;
    }

    /**
     * 设置进程pid
     */
    private static void writePID() {
        PID = SystemUtil.getPid();
    }

    public static void main(String[] args) throws Exception {
        start();
        Stopper.getInstance().registerMBean().doWait();
        stop();
        Stopper.getInstance().stopDone();
        ConsoleLogger.info("CLILOG BYE.");
    }

    /**
     * 
     * @throws Exception
     */
    public static void start() throws Exception {
        writePID();
        try {
            // 1.设置系统属性,加载配置，初始化log4j
            ResourcesManager.getInstance().load();

            SystemUtil.writerPid(PID);

            // 2.启动 log 服务
            LogCenter.getInstance().start();

            // 3.启动netty server
            GlobalNettyServer.getInstance().start();

            // 4.启动 jmx server
            jmxServer =
                new JMXServer(SystemConfig.getInstance().getRmiPort(), SystemConfig.getInstance().getRmiServerPort());
            jmxServer.start();
        } catch (Throwable e) {
            ConsoleLogger.error("start error. ", e);
            System.exit(-1);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public static void stop() {
        try {
            // 5.netty关闭服务
            GlobalNettyServer.getInstance().stop();
        } catch (Throwable e) {
            e.printStackTrace();
            SystemLogger.error("GlobalNettyServer stop error. ", e);
        }
        try {
            // 6.关闭 log服务
            LogCenter.getInstance().stop();
        } catch (Throwable e) {
            ConsoleLogger.error("stop error. ", e);
        }
        try {
            jmxServer.stop();
            MBeans.unregisterAll();
            // 7.卸载资源
            ResourcesManager.getInstance().unload();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
