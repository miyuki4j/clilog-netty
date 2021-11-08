package com.gow.clilog.log.data;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @description: 接受到的一条log
 */
public interface LogData<T> extends Comparable<LogData> {
    /**
     * 这条log的创建时间
     * 
     * @return
     */
    long createTime();

    /**
     * 这条数据所属logger name
     * 
     * @return
     */
    String loggerName();

    LogData loggerName(String loggerName);

    /**
     * 设置 log data create time
     * 
     * @param createTime
     * @return
     */
    LogData createTime(long createTime);

    /**
     * 这条log的对端地址
     * 
     * @return
     */
    String remoteAddress();

    /**
     * 设置远端地址
     * 
     * @param remoteAddres
     * @return
     */
    LogData remoteAddress(String remoteAddres);

    /**
     * 接受到 log 数据源
     * 
     * @return
     */
    T source();

    /**
     * source 转 string
     * 
     * @return
     * @throws Exception
     */
    String source2Text() throws Exception;

    /**
     * 输出到log文件的字符串
     *
     * @return
     */
    String toLogText() throws Exception;

    /**
     * 带参时间戳,输出到log文件的字符串
     *
     * @param createTimeFormat
     *            这条log到来的时间戳
     * @return
     */
    String toLogText(String createTimeFormat) throws Exception;

    /**
     * 检测内容是否为空
     * 
     * @return
     */

    boolean isEmpty();

    /**
     * 清空内容
     */
    void clear();

}
