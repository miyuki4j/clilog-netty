package com.gow.clilog.netty;

import com.gow.clilog.conf.SystemConfig;
import com.gow.clilog.netty.server.NettyServer;
import com.gow.clilog.netty.type.NettyServerEnum;
import com.gow.clilog.util.SystemLogger;

public final class GlobalNettyServer {
    static GlobalNettyServer instance = new GlobalNettyServer();
    private NettyServer globalServer;

    private GlobalNettyServer() {
    }

    public static GlobalNettyServer getInstance() {
        return instance;
    }

    /**
     * @throws Exception
     */
    public synchronized void start() throws Exception {
        NettyServerEnum serverEnum = NettyServerEnum.find(SystemConfig.getInstance().getNettyServerType());
        this.globalServer = serverEnum.generateServer(SystemConfig.getInstance().getNettyServerType(),
                SystemConfig.getInstance().getNettyServerPort());
        this.globalServer.build().start();
        SystemLogger.info(this.globalServer.name() + " start port=" + this.globalServer.port());
    }

    /**
     * @throws Exception
     */
    public synchronized void stop() throws Exception {
        this.globalServer.stop().destory();
        SystemLogger.info(this.globalServer.name() + " to stop");
    }
}
