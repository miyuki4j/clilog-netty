package com.zhe.lock.spin.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.zhe.lock.spin.SpinLock;



/**
 * @author zhouhe
 * @date 2021/9/14
 */

/**
 * classic FIFO Wait Queue Spin Lock (CLH)
 * 
 * 简单的可重入互斥自旋锁
 */

@SuppressWarnings("all")
public class CLHReentrantSpinLock implements SpinLock {

    static final AtomicInteger counter = new AtomicInteger(1);

    static class QNode {
        private final int count; // 打印使用，第几个创建的QNode
        volatile boolean locked;
        AtomicInteger lockedCnt = new AtomicInteger(0);

        QNode() {
            count = counter.getAndIncrement();
        }

        @Override
        public String toString() {
            return "(QNode_" + count + "_locked:" + locked + ")";
        }
    }

    /** 尾巴，是所有线程共有的一个。所有线程进来后，把自己设置为tail */
    private final AtomicReference<QNode> tail;
    /** 前驱节点，每个线程独有一个。 */
    private final ThreadLocal<QNode> predNode;
    /** 当前节点，表示自己，每个线程独有一个。 */
    private final ThreadLocal<QNode> localNode;

    public CLHReentrantSpinLock() {
        // TODO 初始状态,tail指向一个新node(head)节点
        this.tail = new AtomicReference<>(new QNode());
        this.localNode = ThreadLocal.withInitial(QNode::new);
        this.predNode = new ThreadLocal<>();
    }

    void peekNodeInfo(String text) {
        System.out.println(Thread.currentThread().getName() + " " + text + ". " + "localNode" + localNode.get() + ", "
            + "predNode" + predNode.get());
    }

    @Override
    public void lock() {
        // TODO 获取当前线程的代表节点,第一次进将获取构造函数中的那个new QNode
        QNode local_node = localNode.get();
        local_node.lockedCnt.incrementAndGet();
        if (local_node.locked) {
            return;
        }
        // TODO 将自己的状态设置为true表示获取锁。
        local_node.locked = true;
        // TODO 将自己放在队列的尾巴，并且返回以前的值。
        QNode pred = tail.getAndSet(local_node);
        // TODO 把旧的节点放入前驱节点。
        predNode.set(pred);
        // TODO 在等待前驱节点的locked域变为false，这是一个自旋等待的过程
        while (pred.locked) {
            // TODO 让出调度
            Thread.yield();
        }
    }

    /**
     * 将当前node指向前驱node，这样操作等于把当前node从链表头部删除（并不是被JVM回收），
     *
     * lock方法中再也拿不到当前Node的引用了，当前线程若要在unlock之后再次拿锁需重新排队
     *
     * 但是每个线程自己都维护了两个QNode，一个在释放锁的时候把当前node置为前驱node，另一个在lock方法的时候重新获取尾node作为前驱node，
     *
     * 如果所有的任务都是由固定数量的线程池执行的话，你会看到所有的QNode的使用会形成一个环形链表（实际不是）
     */
    @Override
    public void unlock() {
        // unlock. 获取自己的node。把自己的locked设置为false。
        QNode node = localNode.get();
        if (node.lockedCnt.decrementAndGet() == 0) {
            node.locked = false;
            localNode.set(predNode.get());
        }
    }
}