package com.gow.clilog.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class BaseNettyServer<B extends BaseNettyServer> implements NettyServer {
    protected final String name;
    protected int port = -1;

    protected ServerBootstrap serverBootstrap;

    protected EventLoopGroup bossGroup;

    protected EventLoopGroup workerGroup;

    protected Class<? extends ServerChannel> channelClazz;

    protected ChannelHandler bossHandler;

    protected ChannelInitializer<SocketChannel> workerInitializer;

    protected Map<ChannelOption, Object> optionMap = new HashMap<>();

    protected Map<ChannelOption, Object> childOptionMap = new HashMap<>();

    protected Map<AttributeKey, Object> attrMap = new HashMap<>();

    protected Map<AttributeKey, Object> childAttrMap = new HashMap<>();

    protected Channel buildedServerChannel; //build 之后的channel

    protected volatile boolean running = false;

    protected BaseNettyServer(String name, int port) {
        this.name = name;
        this.port = port;
        this.bossGroup = this.initBossGroup();
        this.workerGroup = this.initWorkerGroup();
        this.channelClazz = this.initChannelClass();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public B port(int port) {
        this.port = port;
        return self();
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public B option(ChannelOption key, Object value) {
        this.optionMap.putIfAbsent(key, value);
        return self();
    }

    @Override
    public B attr(AttributeKey key, Object value) {
        this.attrMap.putIfAbsent(key, value);
        return self();
    }

    @Override
    public B childOption(ChannelOption key, Object value) {
        this.childOptionMap.putIfAbsent(key, value);
        return self();
    }

    @Override
    public B childAttr(AttributeKey key, Object value) {
        this.childAttrMap.putIfAbsent(key, value);
        return self();
    }

    @Override
    public B bossHandler(ChannelHandler bossHandler) throws Exception {
        assert bossHandler != null;
        this.bossHandler = bossHandler;
        return self();
    }

    @Override
    public B workerChannelInitializer(ChannelInitializer<SocketChannel> workerInitializer) throws Exception {
        assert workerInitializer != null;
        this.workerInitializer = workerInitializer;
        return self();
    }

    @Override
    public B build() throws Exception {
        checkBeforeBuild();
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(this.bossGroup, this.workerGroup);
        this.initChannelClazz()
                .initBossHandler()
                .initChannelInitializer()
                .initAttr()
                .initOption()
                .initChildAttr()
                .initChildOption();
        checkAfterBuild();
        return self();
    }

    @Override
    public synchronized B start() throws Exception {
        if (isRunning()) {
            return self();
        }
        this.buildedServerChannel = this.serverBootstrap.bind(port).sync().channel();
        return switchState(true);
    }

    @Override
    public synchronized B stop() throws Exception {
        if (!isRunning()) {
            return self();
        }
        buildedServerChannel.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        return switchState(false);
    }

    @Override
    public synchronized B destory() throws Exception {
        if (isRunning()) {
            throw new RuntimeException(name() + " is running , please stop before destory.");
        }
        if (this.optionMap != null) {
            this.optionMap.clear();
        }
        if (this.attrMap != null) {
            this.attrMap.clear();
        }
        if (this.childOptionMap != null) {
            this.childOptionMap.clear();
        }
        if (this.childAttrMap != null) {
            this.childAttrMap.clear();
        }
        this.serverBootstrap = null;
        this.buildedServerChannel = null;
        this.bossGroup = null;
        this.workerGroup = null;
        this.channelClazz = null;
        this.bossHandler = null;
        this.workerInitializer = null;
        return self();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public B self() {
        return (B) this;
    }

    //**************************** sub class implements ******************************** //

    /**
     * build 之前的检查工作
     *
     * @throws Exception
     */
    protected void checkBeforeBuild() throws Exception {
        assert this.port >= 1024;
        assert this.bossGroup != null;
        assert this.workerGroup != null;
        assert this.channelClazz != null;
        assert this.workerInitializer != null;
    }

    /**
     * build 之后的检查工作
     *
     * @throws Exception
     */
    protected void checkAfterBuild() throws Exception {

    }

    protected B initChannelClazz() throws Exception {
        if (this.channelClazz != null) {
            this.serverBootstrap.channel(this.channelClazz);
        }
        return self();
    }


    protected B initBossHandler() throws Exception {
        if (this.bossHandler != null) {
            this.serverBootstrap.handler(this.bossHandler);
        }
        return self();
    }

    protected B initChannelInitializer() throws Exception {
        if (this.workerInitializer != null) {
            this.serverBootstrap.childHandler(this.workerInitializer);
        }
        return self();
    }

    /**
     * @return
     * @throws Exception
     */
    protected B initOption() throws Exception {
        if (this.optionMap != null && !this.optionMap.isEmpty()) {
            for (Map.Entry<ChannelOption, Object> entry : optionMap.entrySet()) {
                ChannelOption key = entry.getKey();
                Object value = entry.getValue();
                this.serverBootstrap.option(key, value);
            }
        }
        return self();
    }

    /**
     * @return
     * @throws Exception
     */
    protected B initChildOption() throws Exception {
        if (this.childOptionMap != null && !this.childOptionMap.isEmpty()) {
            for (Map.Entry<ChannelOption, Object> entry : childOptionMap.entrySet()) {
                ChannelOption key = entry.getKey();
                Object value = entry.getValue();
                this.serverBootstrap.childOption(key, value);
            }
        }
        return self();
    }

    /**
     * @return
     * @throws Exception
     */
    protected B initAttr() throws Exception {
        if (this.attrMap != null && !this.attrMap.isEmpty()) {
            for (Map.Entry<AttributeKey, Object> entry : attrMap.entrySet()) {
                AttributeKey key = entry.getKey();
                Object value = entry.getValue();
                this.serverBootstrap.childAttr(key, value);
            }
        }
        return self();
    }

    /**
     * @return
     * @throws Exception
     */
    protected B initChildAttr() throws Exception {
        if (this.childAttrMap != null && !this.childAttrMap.isEmpty()) {
            for (Map.Entry<AttributeKey, Object> entry : childAttrMap.entrySet()) {
                AttributeKey key = entry.getKey();
                Object value = entry.getValue();
                this.serverBootstrap.childAttr(key, value);
            }
        }
        return self();
    }


    /**
     * 子类决定是用那种 boss event loop group
     *
     * @return
     * @throws Exception
     */
    protected abstract EventLoopGroup initBossGroup();

    /**
     * 子类决定是用那种 worker event loop group
     *
     * @return
     * @throws Exception
     */
    protected abstract EventLoopGroup initWorkerGroup();

    /**
     * 子类决定使用何种 channel class
     *
     * @return
     */
    protected abstract Class<? extends ServerChannel> initChannelClass();


    /**
     * @param state
     * @return
     */
    protected B switchState(boolean state) {
        this.running = state;
        return self();
    }

}
