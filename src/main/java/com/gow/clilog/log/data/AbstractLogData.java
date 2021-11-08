package com.gow.clilog.log.data;

/**
 * @author zhouhe
 * @date 2021/9/23
 */
public abstract class AbstractLogData<T> implements LogData {
    protected long createTime;
    protected String loggerName;
    protected String remoteAddress;
    protected Object source;

    protected AbstractLogData(long createTime, T source) {
        this(createTime);
        this.source = source;
    }

    protected AbstractLogData(long createTime) {
        this.createTime = createTime;
    }

    protected AbstractLogData() {
        this(System.currentTimeMillis());
    }

    @Override
    public String loggerName() {
        return loggerName;
    }

    @Override
    public LogData loggerName(String loggerName) {
        this.loggerName = loggerName;
        return this;
    }

    @Override
    public long createTime() {
        return createTime;
    }

    public LogData createTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String remoteAddress() {
        return remoteAddress == null ? "" : remoteAddress;
    }

    public LogData remoteAddress(String address) {
        this.remoteAddress = address;
        return this;
    }

    @Override
    public T source() {
        return (T)source;
    }

    public <V extends AbstractLogData<T>> V source(T source) {
        this.source = source;
        return (V)this;
    }

    @Override
    public boolean isEmpty() {
        return null == source;
    }

    @Override
    public void clear() {
        source = null;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || !(o instanceof LogData)) {
            return -1;
        }
        LogData other = (LogData)o;
        if (this.createTime() < other.createTime()) {
            return -1;
        } else if (this.createTime() > other.createTime()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "AbstractLogData{" + "createTime=" + createTime + ", remoteAddress='" + remoteAddress + '\''
            + ", source=" + source + '}';
    }
}
