package com.gow.clilog.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件写入帮助类
 * 
 * @author Adam
 * @date 2020/05/27
 */
public class QuietWriter {

    static final String LINE_SEP = System.getProperty("line.separator");
    static final String CHARSET_UTF8 = "UTF-8";
    String fileName = null;
    String filePath = null;
    boolean isAppend = true;
    boolean isBackup = false;
    File outFile = null;
    Writer out = null;

    /**
     * 
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名称(含文件扩展名)
     * @throws IOException
     */
    public QuietWriter(String filePath, String fileName) throws IOException {
        this(filePath, fileName, true, false);
    }

    /**
     * 
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名称(含文件扩展名)
     * @param isBackup
     *            若原文件已存在，是否将原文件备份，默认false
     * @param isAppend
     *            是否从文件尾部开始追加，默认 true
     * @throws IOException
     */
    public QuietWriter(String filePath, String fileName, boolean isBackup, boolean isAppend) throws IOException {
        setOptions(filePath, fileName, isBackup, isAppend);
    }

    /**
     * 
     * @param filePath
     * @param fileName
     * @param isBackup
     * @param isAppend
     * @throws IOException
     */
    public synchronized void setOptions(String filePath, String fileName, boolean isBackup, boolean isAppend)
        throws IOException {
        resetOptions();
        this.filePath = filePath;
        this.fileName = fileName;
        this.isBackup = isBackup;
        this.isAppend = isAppend;
        if (!filePath.endsWith(File.separator)) {
            filePath += File.separator;
        }
        String file = filePath + fileName;
        File target = new File(file);
        if (target.exists()) {
            if (isBackup) {// 备份原文件
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
                String date = dateFormat.format(new Date(System.currentTimeMillis()));
                // String newFileName = fileName.substring(0, fileName.indexOf("."))+"_BACKUP"+date+".TXT";
                String bcFileName = filePath + fileName + ".bak" + date;
                File bcFile = new File(bcFileName);
                if (bcFile.exists()) {
                    bcFile.delete();
                }
                target.renameTo(bcFile);
                target = null;
            } else {
                target.delete();
            }
        }
        target = null;
        outFile = new File(file);
        if (!outFile.exists()) {
            String parentName = new File(file).getParent();
            if (parentName != null) {
                File parentDir = new File(parentName);
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
            }
            outFile.createNewFile();
        }
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile, isAppend), CHARSET_UTF8));
    }

    private void resetOptions() {
        this.out = null;
        this.outFile = null;
        this.fileName = null;
        this.filePath = null;
        this.isAppend = true;
        this.isBackup = false;
    }

    public void write(String str) {
        if (str != null && out != null) {
            try {
                out.write(str);
            } catch (Exception e) {
                // "Failed to write ["+string+"]."
            }
        }
    }

    /**
     * 写入换行分割符
     */
    public void writeLineSep() {
        if (out != null) {
            try {
                out.write(LINE_SEP);
            } catch (Exception e) {
                // "Failed to write ["+LINE_SEP+"]."
            }
        }
    }

    /**
     * 关闭写服务
     */
    public void close() {
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            //
        } finally {
            resetOptions();
        }
    }

    /**
     * 获取文件路径
     * 
     * @return
     */
    public String getFilePath() {
        if (outFile != null) {
            return outFile.getPath();
        }
        return "";
    }

}
