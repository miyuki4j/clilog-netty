package com.zhe.lock.spin;

/**
 * @author zhouhe
 * @date 2021/9/14
 */
/** �������ӿ� */
public interface SpinLock {

    void lock();

    void unlock();
}