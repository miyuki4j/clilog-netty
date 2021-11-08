package com.gow.clilog.netty.server.impl;

import com.gow.clilog.netty.server.BaseNettyServer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * nio server
 */
@SuppressWarnings("all")
public class NioNettyServer extends BaseNettyServer<NioNettyServer> {
    protected int workerEventLoopNum = -1;

    public NioNettyServer(String name, int port) {
        super(name, port);
    }

    @Deprecated
    public NioNettyServer(String name, int port, int workerEventLoopNum) {
        super(name, port);
        this.workerEventLoopNum = workerEventLoopNum;
    }

    @Override
    protected EventLoopGroup initBossGroup() {
        return new NioEventLoopGroup();
    }

    @Override
    protected EventLoopGroup initWorkerGroup() {
        return workerEventLoopNum == -1 ? new NioEventLoopGroup() : new NioEventLoopGroup(workerEventLoopNum);
    }

    @Override
    protected Class<? extends ServerChannel> initChannelClass() {
        return NioServerSocketChannel.class;
    }
}
