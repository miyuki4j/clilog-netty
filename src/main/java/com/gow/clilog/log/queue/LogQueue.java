package com.gow.clilog.log.queue;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.service.LogService;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @description:
 */
public interface LogQueue extends Parentable {
    String QUEUE_NAME_SUFFIX = "-LogQueue";

    /**
     * 所属
     * 
     * @return
     */
    LogService parent();

    /**
     * 设置所属
     *
     * @param parent
     */
    void parent(LogService parent);

    /**
     * 队列名称
     * 
     * @return
     */
    String queueName();

    /**
     * 队列所依赖的logger name
     * 
     * @return
     */
    String loggerName();

    /**
     * log queue 初始化
     * 
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * log queue 销毁
     * 
     * @throws Exception
     */
    void destory() throws Exception;

    /**
     * 放入一个 log data
     * 
     * @param logData
     * @throws Exception
     */
    boolean offer(LogData logData) throws Exception;

    /**
     * 放入一个 log data
     * 
     * @param logData
     * @param timeout
     * @param unit
     * @return
     * @throws Exception
     */
    boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception;

    /**
     * 阻塞获取
     * 
     * @return
     * @throws Exception
     */
    LogData poll() throws Exception;

    /**
     * 超时阻塞获取
     * 
     * @param timeout
     * @param unit
     * @return
     * @throws Exception
     */
    LogData poll(long timeout, TimeUnit unit) throws Exception;

    /**
     * 对头元素
     * 
     * @return
     */
    LogData peek();

    /**
     * 内部依赖的阻塞队列
     * 
     * @param <V>
     * @return
     */
    <V extends LogData> Queue<V> innerQueue();

    /**
     * 是否阻塞队列
     * @return
     */
    boolean isBlockQueue();

    /**
     * 队列状态
     * 
     * @return
     */
    String queueState();

    /**
     * 增加接受的计数
     * 
     * @return
     */
    long incOfferCnt();

    /**
     * 增加接受成功的计数
     * 
     * @return
     */

    long incOfferSuccessCnt();

    /**
     * 增加取走的数量
     * 
     * @return
     */
    long incPollCnt();

    /**
     * 队列是否为空
     * 
     * @return
     */
    boolean isEmpty();

    /**
     * 队列大小
     * 
     * @return
     */
    int size();
}
