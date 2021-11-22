package com.zhe.lock.spin.impl;

import com.zhe.lock.spin.SpinLock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;



/**
 * @author zhouhe
 * @date 2021/11/22
 */
// MCS Queue Lock maintains a linked-list for
// threads waiting to enter critical section (CS).
//
// Each thread that wants to enter CS joins at the
// end of the queue, and waits for the thread
// infront of it to finish its CS.
//
// So, it locks itself and asks the thread infront
// of it, to unlock it when he's done. Atomics
// instructions are used when updating the shared
// queue. Corner cases are also takes care of.
//
// As each thread waits (spins) on its own "locked"
// field, this type of lock is suitable for
// cache-less NUMA architectures.

@SuppressWarnings("all")
public class MCSReentrantSpinLock implements SpinLock {
    static final AtomicInteger counter = new AtomicInteger(1);

    static class QNode {
        private final int count; // 打印使用，第几个创建的QNode
        volatile boolean locked;
        volatile QNode next;
        int cnt = 0;

        public QNode() {
            this.count = counter.getAndIncrement();
        }

        public int incrementAndGet() {
            return ++cnt;
        }

        public int decrementAndGet() {
            return --cnt;
        }
    }

    AtomicReference<QNode> queue;
    ThreadLocal<QNode> localNode;
    // queue: points to the tail
    // localNode: is unique for each thread

    public MCSReentrantSpinLock() {
        queue = new AtomicReference<>(null);
        localNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    // 1. When thread wants to access critical
    // section, it stands at the end of the
    // queue (FIFO).
    // 2a. If there is no one in queue, it goes head
    // with its critical section.
    // 2b. Otherwise, it locks itself and asks the
    // thread infront of it to unlock it when its
    // done with CS.
    @Override
    public void lock() {
        QNode n = localNode.get(); // 1
        if (!n.locked && n.cnt > 0) {
            n.incrementAndGet();
            return;
        }
        n.incrementAndGet();
        QNode m = queue.getAndSet(n); // 1
        if (m != null) { // 2b
            n.locked = true; // 2b
            m.next = n; // 2b
            while (n.locked) {
                Thread.yield(); // 2b
            }
        } // 2a
    }

    // 1. When a thread is done with its critical
    // section, it needs to unlock any thread
    // standing behind it.
    // 2a. If there is a thread standing behind,
    // then it unlocks him.
    // 2b. Otherwise it tries to mark queue as empty.
    // If no one is joining, it leaves.
    // 2c. If there is a thread trying to join the
    // queue, it waits until he is done, and then
    // unlocks him, and leaves.
    @Override
    public void unlock() {
        QNode n = localNode.get(); // 1
        if (n.decrementAndGet() <= 0) {
            if (n.next == null) { // 2b
                if (queue.compareAndSet(n, null)) { // 2b
                    n.cnt = 0;
                    return; // 2b
                }
                while (n.next == null) {
                    Thread.yield(); // 2c
                }
            } // 2a
            n.next.locked = false; // 2a
            n.next = null; // 2a
            n.cnt = 0;
        }
    }
}
