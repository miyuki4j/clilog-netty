package com.gow.clilog.log.manager;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.monitor.LogMonitor;
import com.gow.clilog.log.service.LogService;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @description: log 管理器
 */
@SuppressWarnings("all")
public interface LogManager extends Parentable {

    String managerName();

    /**
     * 设置下一个log handle manager
     * 
     * @param nextManager
     */
    void initNextManager(LogManager nextManager)throws Exception;

    /**
     * 返回设置的下一个log handle manager
     * 
     * @param nextManager
     * @return
     */
    LogManager nextManager();

    /**
     * 初始化log管理器，注意在 {@link #start()}之前调用
     * 
     * @param loggerNameSet
     * @throws Exception
     */
    Set<LogService> init(Set<String> loggerNameSet) throws Exception;

    /**
     * 初始化log管理器,注意在 {@link #start()}之前调用
     * 
     * @param loggerName
     * @return
     * @throws Exception
     */
    LogService init(String loggerName) throws Exception;

    /**
     * 开启log管理器
     * 
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 停止log管理器
     * 
     * @throws Exception
     */
    void stop() throws Exception;

    /**
     * 销毁log管理器
     *
     * @throws Exception
     */
    void destory() throws Exception;

    /**
     * 查找logger name 对应的log service
     *
     * @param loggerName
     * @return
     */
    LogService queryService(String loggerName);

    /**
     * 动态添加
     * 
     * @param logService
     * @return
     */
    boolean addService(LogService logService) throws Exception;

    /**
     * 根据名字移除
     * 
     * @param logService
     * @return
     */
    LogService removeService(String loggerName) throws Exception;

    /**
     * 根据实例移除
     * 
     * @param logService
     * @return
     * @throws Exception
     */
    boolean removeService(LogService logService) throws Exception;

    /**
     * 返回 log service list
     * 
     * @return
     */
    Collection<LogService> services();

    /**
     * 返回 log monitor list
     * 
     * @return
     */
    Collection<LogMonitor> monitors();

    /**
     * 尝试热加载 log service
     * 
     * @param loggerNameSet
     */
    Set<LogService> tryReload(Set<String> loggerNameSet) throws Exception;

    /**
     * 注册单个 monitor
     * 
     * @param monitor
     * @return
     * @throws Exception
     */
    boolean registerLogMonitor(LogMonitor monitor) throws Exception;

    /**
     * 注册 log monitor
     * 
     * @param monitors
     * @return
     * @throws Exception
     */
    boolean registerLogMonitor(Collection<LogMonitor> monitors) throws Exception;

    /**
     * 自定义注册,注意在{@link #start()}之后再去注册.
     * 
     * @throws Exception
     */
    void customRegister() throws Exception;

    // *************************************** operation **************************************** //

    /**
     * 向log manager 投递 logdata
     * 
     * @param logData
     * @return
     * @throws Exception
     */
    public boolean offer(LogData logData) throws Exception;

    /**
     * 向log manager 投递 logdata,带超时参数
     * 
     * @param logData
     * @param timeout
     * @param unit
     * @return
     * @throws Exception
     */
    public boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception;

    /**
     * manager 是否运行
     * 
     * @return
     */
    public boolean isRunning();

    /**
     * 是否直接模式
     * 
     * @return
     */
    public boolean isDirectable();

    /**
     * 一键启动
     */
    public void oneKeyOpen() throws Exception;

    /**
     * 一键关闭
     * 
     * @throws Exception
     */
    public void oneKeyClose() throws Exception;

}
