package com.gow.clilog.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;

/**
 * netty server 接口
 */
public interface NettyServer {
    /**
     * 服务器名称
     *
     * @return
     */
    String name();

    /**
     * 设置端口号
     *
     * @param port
     * @return
     */
    NettyServer port(int port);

    /**
     * 返回端口号
     *
     * @return
     */
    int port();

    /**
     * boss group 选项
     *
     * @param key
     * @param value
     * @return
     */
    NettyServer option(ChannelOption key, Object value);

    /**
     * boss group 属性
     *
     * @param key
     * @param value
     * @return
     */
    NettyServer attr(AttributeKey key, Object value);

    /**
     * worker group 属性
     *
     * @param key
     * @param value
     * @return
     */

    NettyServer childOption(ChannelOption key, Object value);

    /**
     * worker
     * group 属性
     *
     * @param key
     * @param value
     * @return
     */
    NettyServer childAttr(AttributeKey key, Object value);

    /**
     * boos group bossHandler
     *
     * @param bossHandler
     * @return
     */
    NettyServer bossHandler(ChannelHandler bossHandler) throws Exception;

    /**
     * work group channel 初始化
     *
     * @param workerInitializer
     * @return
     */
    NettyServer workerChannelInitializer(ChannelInitializer<SocketChannel> workerInitializer) throws Exception;

    /**
     * start 之前的build 工作
     *
     * @return
     * @throws Exception
     */
    NettyServer build() throws Exception;

    /**
     * 启动
     *
     * @return
     * @throws Exception
     */
    NettyServer start() throws Exception;

    /**
     * 停止
     *
     * @return
     * @throws Exception
     */

    NettyServer stop() throws Exception;


    NettyServer destory() throws Exception;

    /**
     * @return
     */
    boolean isRunning();

    /**
     * @return
     */
    NettyServer self();
}
