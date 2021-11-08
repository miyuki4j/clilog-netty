package com.gow.clilog.log.data.impl;

import com.gow.clilog.log.data.AbstractLogData;

/**
 * @author zhouhe
 * @date 2021/9/23
 */
public class PlainLogData extends AbstractLogData<String> {

    public PlainLogData(long createTime, String source) {
        super(createTime, source);
    }

    public PlainLogData(long createTime) {
        super(createTime);
    }

    public PlainLogData() {
        super();
    }

    @Override
    public String source2Text() throws Exception {
        return source();
    }

    @Override
    public String toLogText() {
        return remoteAddress() + " " + source();
    }

    @Override
    public String toLogText(String createTimeFormat) {
        return toLogText();
    }
}
