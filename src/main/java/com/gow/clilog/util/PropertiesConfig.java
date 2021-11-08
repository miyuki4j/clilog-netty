package com.gow.clilog.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties配置抽象类
 * 
 * @author Adam
 * @date 2020/06/04
 */
public abstract class PropertiesConfig {

    protected String filePath;

    protected Properties prop;

    /**
     * 加载配置文件
     * 
     * @param filePath
     *            文件路径
     * @throws Exception
     */
    public void load(String filePath) throws Exception {
        load(new File(filePath));
    }

    /**
     * 加载配置文件
     * 
     * @param file
     *            配置文件
     * @throws Exception
     */
    public void load(File file) throws Exception {
        this.filePath = file.getPath();
        InputStream ins = new BufferedInputStream(new FileInputStream(file));
        prop = new Properties();
        prop.load(ins);
        loadProperties();
        freeStream(ins);
    }

    /**
     * 释放资源流
     * 
     * @param ins
     * @throws Exception
     */
    protected void freeStream(InputStream ins) throws Exception {
        if (null != ins) {
            ins.close();
        }
    }

    /**
     *
     * @throws Exception
     */
    protected void destory() throws Exception {
        if (prop != null) {
            prop.clear();
            prop = null;
        }
    }

    /**
     * 加载Properties数据
     * 
     * @throws Exception
     */
    protected abstract void loadProperties() throws Exception;

    /**
     * 获得文件路径
     * 
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 获得 Properties
     * 
     * @return
     */
    public Properties getProperties() {
        return prop;
    }

}
