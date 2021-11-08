package com.gow.clilog.netty.factory;

import com.gow.clilog.netty.initializer.HttpServerInitializer;
import com.gow.clilog.netty.server.NettyServer;
import com.gow.clilog.netty.server.impl.NioNettyServer;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class NettyServerFactory {
    /**
     * 启动一个 nio server
     *
     * @param serverName
     * @param port
     * @return
     * @throws Exception
     */
    public static NettyServer createDefaultServer(String serverName, int port) throws Exception {
        return new NioNettyServer(serverName, port)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bossHandler(new LoggingHandler(LogLevel.INFO))
                .workerChannelInitializer(new HttpServerInitializer());
    }
}
