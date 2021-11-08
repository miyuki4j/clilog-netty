package com.gow.clilog.mbean;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.gow.clilog.util.SystemLogger;

public class MBeans {
    ////////////////////////////////////////////////////////
    private static Manager local = new Manager();
    ////////////////////////////////////////////////////////

    /**
     * 
     * @param name
     * @return
     */
    public static ObjectName newObjectName(String name) {
        try {
            return new ObjectName(name);
        } catch (Throwable e) {
            SystemLogger.error("newObjectName", e);
            throw new RuntimeException(e);
        }
    }

    public static ObjectName register(Object object, String name) {
        return local.register(object, name);
    }

    public static void unregisterAll() {
        local.unregisterAll();
    }

    public static void unregister(ObjectName objname) {
        local.unregister(objname);
    }

    public static class Manager {
        private final java.util.Set<ObjectName> mbeans = new java.util.HashSet<ObjectName>();

        public ObjectName register(Object object, String name) {
            synchronized (mbeans) {
                ObjectName objName = newObjectName(name);
                if (!mbeans.add(objName)) {
                    throw new RuntimeException("duplicate mbean name of '" + objName + "'");
                }
                try {
                    MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
                    server.registerMBean(object, objName);
                    return objName;
                } catch (Throwable e) {
                    mbeans.remove(objName);
                    SystemLogger.error("registerMBean", e);
                    throw new RuntimeException("see xdb.Xdb.registerMBean", e);
                }
            }
        }

        public void unregisterAll() {
            synchronized (mbeans) {
                MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
                for (ObjectName name : mbeans) {
                    try {
                        server.unregisterMBean(name);
                    } catch (Throwable e) {
                        SystemLogger.error("unregisterMBean name=" + name, e);
                    }
                }
                mbeans.clear();
            }
        }

        public void unregister(ObjectName objname) {
            synchronized (mbeans) {
                try {
                    if (mbeans.remove(objname)) {
                        MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
                        server.unregisterMBean(objname);
                    }
                } catch (Throwable e) {
                    SystemLogger.error("unregisterMBean name=" + objname, e);
                }
            }
        }
    }
}
