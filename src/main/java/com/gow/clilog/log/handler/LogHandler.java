package com.gow.clilog.log.handler;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.service.LogService;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @description:
 */
@SuppressWarnings("all")
public interface LogHandler extends Parentable {
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
     * handler的名字
     * 
     * @return
     */
    String handlerName();

    /**
     * 依赖的logger的名字
     * 
     * @return
     */
    String loggerName();

    /**
     * 处理log data
     * 
     * @param logData
     * @throws Exception
     */
    void handle(LogData logData) throws Exception;

    /**
     * 初始化工作
     * 
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 销毁工作
     * 
     * @throws Exception
     */
    void destory() throws Exception;

    /**
     * 增加处理的计数
     *
     * @return
     */
    long incHandleCnt();

    /**
     * 增加处理成功的计数
     *
     * @return
     */
    long incHandleSuccessCnt();


    String handlerState();
}
