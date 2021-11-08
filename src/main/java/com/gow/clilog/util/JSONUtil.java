package com.gow.clilog.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 * JSON帮助类
 * 
 * @author Adam
 * @date 2020/06/09
 */
public final class JSONUtil {

    private static ObjectMapper defaultMapper = new ObjectMapper();

    static {
        defaultMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * JSON帮助类初始化
     */
    public static void init() {
        // do nothing
    }

    /**
     * 获得ObjectMapper
     * 
     * @return
     */
    public static ObjectMapper jsonMapper() {
        return defaultMapper;
    }

    /**
     * 创建ObjectMapper
     * 
     * @return
     */
    public static ObjectMapper createMapper() {
        return new ObjectMapper();
    }

    /**
     * 将JSON转换为Node
     *
     * @param json
     * @return
     */
    public static JsonNode parse2Node(String json) {
        return parse2Node(json, null);
    }

    /**
     * 将JSON转换为Node
     *
     * @param json
     * @param rootName
     *            root name
     * @return
     */
    public static JsonNode parse2Node(String json, String rootName) {
        if (isBlank(json)) {
            return null;
        }
        try {
            JsonNode node = defaultMapper.readTree(json);
            if (isBlank(rootName)) {
                return node;
            }
            return node.get(rootName);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

}
