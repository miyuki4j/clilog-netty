package com.gow.clilog.log.service.impl;

import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.handler.LogHandler;
import com.gow.clilog.log.monitor.OnceTimePair;
import com.gow.clilog.log.queue.LogQueue;
import com.gow.clilog.log.service.AbstractLogService;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouhe
 * @date 2021/9/27
 * @descript: 默认的log 服务
 */
public class DefaultLogService extends AbstractLogService {

    public DefaultLogService(String loggerName, LogQueue logQueue, LogHandler logHandler, OnceTimePair pollTimePair) {
        super(loggerName, logQueue, logHandler, pollTimePair);
    }

    public DefaultLogService(String loggerName, String serviceName, LogQueue logQueue, LogHandler logHandler,
                             OnceTimePair pollTimePair) {
        super(loggerName, serviceName, logQueue, logHandler, pollTimePair);
    }

    public DefaultLogService(String loggerName, String serviceName, LogQueue logQueue, LogHandler logHandler) {
        super(loggerName, serviceName, logQueue, logHandler, new OnceTimePair(3L, TimeUnit.SECONDS));
    }

    @Override
    protected void onConstructor() {
        super.onConstructor();
    }

    @Override
    protected void initOtherParam() throws Exception {
        super.initOtherParam();
    }

    @Override
    protected void onRan(Thread curThread) {
        super.onRan(curThread);
    }

    @Override
    protected void runLoop(final Thread curThread) throws Exception {
        while (isRunning() || !logQueue().isEmpty()) {
            LogData data = null;
            try {
                data = logQueue.poll(pollTimePair.getDelay(), pollTimePair.getUnit());
                if (data != null) {
                    logHandler.handle(data);
                    logHandler().incHandleSuccessCnt();
                }
            } catch (NullPointerException npe) {
                data = null;
            } catch (InterruptedException ie) {
                // skip
            } catch (Throwable e) {
                // skip
            } finally {
                if (data != null) {
                    logHandler().incHandleCnt();
                }
            }
        }
    }

}
