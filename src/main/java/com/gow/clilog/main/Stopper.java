package com.gow.clilog.main;

import java.util.Calendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.gow.clilog.mbean.MBeans;
import com.gow.clilog.util.SystemLogger;

public class Stopper implements StopperMXBean {
    public final static Stopper instance = new Stopper();
    static transient final ReentrantLock shutdownAlarmLock = new ReentrantLock();
    static transient final Condition shutdownAlarm = shutdownAlarmLock.newCondition();
    static transient final ReentrantLock shutdownCompletedLock = new ReentrantLock();
    static transient final Condition shutdownCompleted = shutdownCompletedLock.newCondition();
    public static long stopTime = -1L;

    public Stopper() {

    }

    public static Stopper getInstance() {
        return instance;
    }

    /**
     * 注册一个停止钩子
     *
     * @return
     * @throws Exception
     */
    public Stopper registerMBean() throws Exception {
        MBeans.register(this, "bean:name=stopper");
        return this;

    }

    public void doWait() {
        while (true) {
            try {
                shutdownAlarmLock.lockInterruptibly();
            } catch (final InterruptedException ex) {
                break;
            }
            try {
                if (stopTime < 0) {
                    shutdownAlarm.await();
                } else {
                    final long now = Calendar.getInstance().getTimeInMillis();
                    if (now >= stopTime) {
                        break;
                    } else {
                        shutdownAlarm.awaitUntil(new java.util.Date(stopTime));
                    }
                }
            } catch (final InterruptedException ex) {
                break;
            } finally {
                shutdownAlarmLock.unlock();
            }
        }
    }

    public long getStopTime() {
        final long time;
        shutdownAlarmLock.lock();
        try {
            time = stopTime;
        } finally {
            shutdownAlarmLock.unlock();
        }
        if (time <= 0) {
            return time;
        } else {
            return (time - Calendar.getInstance().getTimeInMillis()) / 1000;
        }
    }

    public void setStopTime(long seconds) {
        if (seconds < 0) {
            return;
        }
        shutdownAlarmLock.lock();
        try {
            stopTime = Calendar.getInstance().getTimeInMillis() + seconds * 1000L;
            shutdownAlarm.signalAll();
        } finally {
            shutdownAlarmLock.unlock();
        }

    }

    /**
     * 同步关闭，等到服务器彻底关闭完成后，才返回
     *
     * @param seconds
     */
    public void stop(int seconds) {
        setStopTime(seconds);
        try {
            shutdownCompletedLock.lockInterruptibly();
        } catch (final InterruptedException ex) {
            return;
        }
        try {
            shutdownCompleted.await();
        } catch (final InterruptedException ex) {
        } finally {
            shutdownCompletedLock.unlock();
        }
    }

    protected void onStop() {
        try {
            shutdownCompletedLock.lock();
            try {
                shutdownCompleted.signalAll();
            } finally {
                shutdownCompletedLock.unlock();
            }
            SystemLogger.info("stopper to terminated.");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void stopDone() {
        shutdownCompletedLock.lock();
        try {
            shutdownCompleted.signalAll();
        } finally {
            shutdownCompletedLock.unlock();
        }
    }
}
