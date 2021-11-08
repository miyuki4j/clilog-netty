package com.gow.clilog.util;

import java.io.File;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 系统帮助类
 * 
 * @author Adam
 * @date 2020/05/27
 */
@SuppressWarnings("all")
public class SystemUtil {
    /**
     * 系统换行符
     */
    public static final String LINE_SEP = System.getProperty("line.separator");
    /**
     * 进程ID文件名
     */
    public static final String PID_FILE_NAME = "this.pid";

    /**
     * 获取操作系统信号类型
     * 
     * @return
     */
    public static String getOSSignalType() {
        return System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "USR2";
    }

    /**
     * 获取当前进程ID
     * 
     * @return
     */
    public static int getPid() {
        int pid = -1;
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            if (runtime.getName() != null && runtime.getName().contains("@")) {
                String[] rtNames = runtime.getName().split("@");
                if (rtNames != null && rtNames.length > 0) {
                    pid = Integer.parseInt(rtNames[0]);
                }
            }
        } catch (Exception e) {
            SystemLogger.error("SystemUtil get process id error.", e);
        }
        return pid;
    }

    /**
     * 写入进程ID至文件中
     * 
     * @param pid
     */
    public static void writerPid(int pid) {
        QuietWriter writer = null;
        try {
            String fileDir = getCurrentPath();
            writer = new QuietWriter(fileDir, PID_FILE_NAME, false, false);
            writer.write(Integer.toString(pid));
        } catch (Exception e) {
            SystemLogger.error("SystemUtil writer process id error. the pid:" + pid, e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 获取当前所在路径
     * 
     * @return
     * @throws Exception
     */
    public static String getCurrentPath() throws Exception {
        return new File(".").getCanonicalPath();
    }

    /**
     * 应用程序是否为虚拟机启动
     * 
     * @return
     */
    public static boolean inVM() {
        File fromFile = new File(SystemUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (null != fromFile && fromFile.isFile() && fromFile.getName().endsWith(".jar")) {
            return true;
        }
        return false;
    }

    /**
     * 应用程序是否为集成开发环境启动
     * 
     * @return
     */
    public static boolean inIDE() {
        return !inVM();
    }

    /**
     * 字符串分割
     * 
     * @param source
     * @param delimiter
     * @return
     */
    public static Set<String> parseStrSet(String source, String delimiter) {
        Set<String> set = new LinkedHashSet<>();
        if (source == null || source.isEmpty()) {
            return set;
        }
        for (String s : source.split(delimiter)) {
            set.add(s);
        }
        return set;
    }

    /**
     * 系统统计
     * 
     * @author Adam
     * @date 2020/12/04
     */
    public static class SystemStats implements Serializable {

        private static final long serialVersionUID = 1L;

    }

}
