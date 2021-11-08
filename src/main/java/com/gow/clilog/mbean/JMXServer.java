package com.gow.clilog.mbean;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import com.gow.clilog.util.SystemLogger;

/**
 * Usage:
 * 
 * <pre>
 * main() {
 *     JMXServer js = new JMXServer(...);
 *     js.start();
 *     ...
 *     js.stop();
 * }
 * </pre>
 * 
 * <p>
 * As you may already know if you have been confronted with this problem, the JMX RMI connector opens two ports: one is
 * for the RMI registry, and it's the port that you usually supply with the -Dcom.sun.management.jmxremote.port=<port>
 * property. The other port is used to export JMX RMI connection objects. This second port is usually dynamically
 * allocated at random. Indeed you don't need to know this port number in order to connect to the JMX agent: the only
 * port number you need to know to connect is the RMI registry port number from which to obtain the connection objects.
 * <p>
 * This however can prove to be troublesome if your application is behind a firewall that block access to random ports.
 * The default JVM agent will not let you specify that second port number, and you're stuck. The only way to specify
 * that second port number is to use a JMXServiceURL, but you can't supply a JMXServiceURL to the default agent.
 */
public class JMXServer {
    private JMXConnectorServer cs;
    private volatile boolean running = false;

    public JMXServer() throws IOException {
        this(1098);
    }

    /**
     * 
     * @param rmiPort
     * @throws IOException
     */
    public JMXServer(int rmiPort) throws IOException {
        this(rmiPort, rmiPort + 1, "127.0.0.1", null, null);
    }

    public JMXServer(int rmiPort, int rmiServerPort) throws IOException {
        this(rmiPort, rmiServerPort, "127.0.0.1", null, null);
    }

    public JMXServer(int rmiport, int serverport, String hostname, String passwordfile, String accessfile)
        throws IOException {

        String property = System.getProperty("\"java.rmi.server.hostname\"");
        if (null != hostname) {
            System.setProperty("java.rmi.server.hostname", hostname);
        }
        LocateRegistry.createRegistry(rmiport);
        JMXServiceURL url = new JMXServiceURL(
            String.format("service:jmx:rmi://localhost:%d/jndi/rmi://localhost:%d/jmxrmi", serverport, rmiport));

        Map<String, Object> env = new HashMap<String, Object>();
        if (null != passwordfile) {
            env.put("jmx.remote.x.password.file", passwordfile);
        }
        if (null != accessfile) {
            env.put("jmx.remote.x.access.file", accessfile);
        }
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        cs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
    }

    public synchronized void start() throws IOException {
        if (running) {
            return;
        }
        cs.start();
        running = true;
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        try {
            cs.stop();
        } catch (Exception e) {
            SystemLogger.error("stop", e);
        } finally {
            running = false;
        }
    }
}
