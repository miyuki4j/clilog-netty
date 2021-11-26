package com.gow.clilog.log.queue;

import com.gow.clilog.log.Parentable;
import com.gow.clilog.log.data.LogData;
import com.gow.clilog.log.service.LogService;
import com.gow.clilog.util.SystemLogger;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhouhe
 * @date 2021/9/27
 */
public abstract class AbstractLogQueue implements LogQueue {

    protected final String queueName;

    protected final String loggerName;

    protected Queue<LogData> logQueue;

    protected LogService parent;

    // 请求计数
    protected AtomicLong offerCounterAll = new AtomicLong();
    protected AtomicLong offerCounterSucc = new AtomicLong();

    // 取走数量
    protected AtomicLong pollCounter = new AtomicLong();

    protected AbstractLogQueue(String queueName, String loggerName, Queue<LogData> logQueue) {
        this.queueName = queueName;
        this.loggerName = loggerName;
        this.logQueue = logQueue;
    }

    protected AbstractLogQueue(String loggerName, Queue<LogData> logQueue) {
        this(loggerName + QUEUE_NAME_SUFFIX, loggerName, logQueue);
    }

    @Override
    public String queueName() {
        return queueName;
    }

    @Override
    public String loggerName() {
        return loggerName;
    }

    @Override
    public void init() throws Exception {
        if (logQueue == null) {
            throw new IllegalStateException(queueName + " the log queue is empty.");
        }
        initOtherParam();
    }

    @Override
    public void destory() throws Exception {
        logQueue.clear();
        logQueue = null;
        unParent();
    }

    @Override
    public boolean offer(LogData logData) throws Exception {
        if (null == logData || logData.source() == null) {
            return false;
        }
        if (isBlockQueue()) {
            try {
                blockingQueue().put(logData);
                incOfferSuccessCnt();
            } catch (InterruptedException ie) {
                return false;
            } catch (Throwable e) {
                throw e;
            } finally {
                incOfferCnt();
            }
        } else {
            try {
                if (this.logQueue.offer(logData)) {
                    incOfferSuccessCnt();
                }
            } catch (Throwable e) {
                throw e;
            } finally {
                incOfferCnt();
            }
        }
        return true;
    }

    @Override
    public boolean offer(LogData logData, long timeout, TimeUnit unit) throws Exception {
        if (null == logData || logData.source() == null) {
            return false;
        }
        if (isBlockQueue()) {
            try {
                if (blockingQueue().offer(logData, timeout, unit)) {
                    incOfferSuccessCnt();
                }
            } catch (InterruptedException ie) {
                return false;
            } catch (Throwable e) {
                throw e;
            } finally {
                incOfferCnt();
            }
        } else {
            try {
                if (blockingQueue().offer(logData)) {
                    incOfferSuccessCnt();
                }
            } catch (Throwable e) {
                throw e;
            } finally {
                incOfferCnt();
            }
        }
        return true;
    }

    @Override
    public LogData poll() throws Exception {
        if (logQueue == null) {
            return null;
        }
        LogData poll = null;
        if (isBlockQueue()) {
            try {
                poll = blockingQueue().take();
            } catch (Throwable e) {
                throw e;
            } finally {
                if (poll != null) {
                    incPollCnt();
                }
            }
        } else {
            try {
                poll = logQueue.poll();
            } catch (Throwable e) {
                throw e;
            } finally {
                if (poll != null) {
                    incPollCnt();
                }
            }
        }
        return poll;
    }

    @Override
    public LogData poll(long timeout, TimeUnit unit) throws Exception {
        if (logQueue == null) {
            return null;
        }
        if (unit == null) {
            throw new NullPointerException(queueName + " >>> poll unit is empty.");
        }
        LogData poll = null;
        if (isBlockQueue()) {
            try {
                poll = ((BlockingQueue<LogData>)logQueue).poll(Math.max(timeout, 0L), unit);
            } finally {
                if (poll != null) {
                    incPollCnt();
                }
            }
        } else {
            try {
                poll = logQueue.poll();
                if (poll == null) {
                    try {
                        unit.sleep(Math.max(timeout, 0L));
                    } catch (InterruptedException ie) {
                        // skip
                    }
                    poll = logQueue.poll();
                }
            } finally {
                if (poll != null) {
                    incPollCnt();
                }
            }
        }
        return poll;
    }

    @Override
    public LogData peek() {
        return logQueue == null ? null : logQueue.peek();
    }

    @Override
    public <V extends LogData> Queue<V> innerQueue() {
        return (Queue<V>)logQueue;
    }

    @Override
    public boolean isBlockQueue() {
        return logQueue instanceof BlockingQueue;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public long getOfferCounterAll() {
        return offerCounterAll.get();
    }

    public long getOfferCounterSucc() {
        return offerCounterSucc.get();
    }

    public AtomicLong getPollCounter() {
        return pollCounter;
    }

    public Queue<LogData> getLogQueue() {
        return logQueue;
    }

    public LogService getParent() {
        return parent;
    }

    @Override
    public String queueState() {
        return String.format("curQueueSize=[%d], offerCountAll=[%d], offerCountSucc=[%d], pollCounter=[%d]",
            logQueue.size(), offerCounterAll.get(), offerCounterSucc.get(), pollCounter.get());
    }

    @Override
    public boolean isEmpty() {
        return logQueue == null || logQueue.isEmpty();
    }

    @Override
    public int size() {
        return isEmpty() ? 0 : logQueue.size();
    }

    @Override
    public LogService parent() {
        return parent;
    }

    @Override
    public void parent(LogService parent) {
        if (parent == null) {
            throw new NullPointerException(queueName() + " >>> queue set parent(service) is null.");
        }
        this.parent = parent;
    }

    @Override
    public void parent(Parentable p) {
        parent((LogService)p);
    }

    @Override
    public void unParent() {
        this.parent = null;
    }

    @Override
    public long incOfferCnt() {
        return offerCounterAll.incrementAndGet();
    }

    @Override
    public long incOfferSuccessCnt() {
        return offerCounterSucc.incrementAndGet();
    }

    @Override
    public long incPollCnt() {
        return pollCounter.incrementAndGet();
    }

    // ***************************************** sub class implements ***************************************//

    protected <V extends LogData> BlockingQueue<V> blockingQueue() {
        return (BlockingQueue<V>)logQueue;
    }

    /**
     * 其他参数的初始化工作
     *
     * @throws Exception
     */
    protected void initOtherParam() throws Exception {
        SystemLogger.info(queueName + " >>> initOtherParam.");
    }
}
