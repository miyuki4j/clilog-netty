package com.gow.clilog.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 属性资源文件帮助类
 * 
 * @author Adam
 * @date 2020/05/28
 */
public final class PropertiesUtil {

    /**
     * 
     * @description: 加载指定的Properties文件，并返回Properties对象
     * @param filePath
     *            文件路径
     * @return
     */
    public static Properties loadProperties(String filePath) {
        Properties prop = null;
        InputStream is = null;
        try {
            prop = new Properties();
            is = new FileInputStream(filePath);
            prop.load(is);
        } catch (Exception e) {
            SystemLogger.error("PropertiesUtil load properties error. the file:" + filePath, e);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception ignored) {
            }
        }
        return prop;
    }

    /**
     * @description: 加载指定的Properties文件，并返回Properties对象
     * @param file
     * @return
     */
    public static Properties loadProperties(File file) {
        Properties prop = null;
        InputStream is = null;
        try {
            prop = new Properties();
            is = new BufferedInputStream(new FileInputStream(file));
            if (null != is) {
                prop.load(is);
            }
        } catch (Exception e) {
            SystemLogger.error("PropertiesUtil load properties error. the file:" + file.getName(), e);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception e2) {
            }
        }
        return prop;
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, String> getPropertiesMap(Properties prop) {
        Map<String, String> propMap = new HashMap<String, String>();
        Iterator iterator = prop.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (null != key && null != value) {
                propMap.put(String.valueOf(key), String.valueOf(value));
            }
        }
        return propMap;
    }

}
