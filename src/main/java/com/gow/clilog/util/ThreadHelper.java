package com.gow.clilog.util;

public class ThreadHelper extends Thread {
    protected volatile boolean running = true;
    protected boolean idle = true;

    public ThreadHelper(String name) {
        this(name, true);
    }

    public ThreadHelper(String name, boolean daemon) {
        super(name);
        this.setDaemon(daemon);
    }

    public ThreadHelper(Runnable target, String name) {
        super(target, name);
    }

    public ThreadHelper daemon(boolean daemon) {
        this.setDaemon(daemon);
        return this;
    }

    public final boolean isRunning() {
        return running;
    }


    public final void joinAssuring() {
        while (true) {
            try {
                join();
                break;
            } catch (Throwable e) {

            }
        }
    }

    public void shutdown() {
        running = false;
        wakeup();
        joinAssuring();
    }

    public void interruptShutdown() {
        running = false;
        this.interrupt();
        joinAssuring();
    }

    public synchronized void wakeup() {
        idle = false;
        this.notify();
    }


    public final synchronized void sleepIdle(long ms) {
        try {
            if (idle) {
                this.wait(ms);
            }
        } catch (InterruptedException e) {

        } finally {
            idle = true;
        }
    }

}
