package com.gow.clilog.netty.type;

import com.gow.clilog.netty.factory.NettyServerFactory;
import com.gow.clilog.netty.server.NettyServer;

import java.util.HashMap;
import java.util.Map;

public enum NettyServerEnum {
    NIO_SERVER("nio", "构造一个nio的server") {
        @Override
        public NettyServer generateServer(String name, int port) throws Exception {
            return NettyServerFactory.createDefaultServer(name, port);
        }
    },
    ;

    public final String type;
    public final String desc;

    static Map<String, NettyServerEnum> map = new HashMap<>();

    static {

    }

    static void init() {
        for (NettyServerEnum value : values()) {
            map.put(value.type, value);
        }
    }

    NettyServerEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String check(String property) {
        return property;
    }

    public static NettyServerEnum find(String type) {
        return map.getOrDefault(type, NIO_SERVER);
    }

    public abstract NettyServer generateServer(String name, int port) throws Exception;
}
