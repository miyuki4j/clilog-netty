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
 * �򵥵Ŀ����뻥��������
 */

@SuppressWarnings("all")
public class CLHReentrantSpinLock implements SpinLock {

    static final AtomicInteger counter = new AtomicInteger(1);

    static class QNode {
        private final int count; // ��ӡʹ�ã��ڼ���������QNode
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

    /** β�ͣ��������̹߳��е�һ���������߳̽����󣬰��Լ�����Ϊtail */
    private final AtomicReference<QNode> tail;
    /** ǰ���ڵ㣬ÿ���̶߳���һ���� */
    private final ThreadLocal<QNode> predNode;
    /** ��ǰ�ڵ㣬��ʾ�Լ���ÿ���̶߳���һ���� */
    private final ThreadLocal<QNode> localNode;

    public CLHReentrantSpinLock() {
        // TODO ��ʼ״̬,tailָ��һ����node(head)�ڵ�
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
        // TODO ��ȡ��ǰ�̵߳Ĵ���ڵ�,��һ�ν�����ȡ���캯���е��Ǹ�new QNode
        QNode local_node = localNode.get();
        local_node.lockedCnt.incrementAndGet();
        if (local_node.locked) {
            return;
        }
        // TODO ���Լ���״̬����Ϊtrue��ʾ��ȡ����
        local_node.locked = true;
        // TODO ���Լ����ڶ��е�β�ͣ����ҷ�����ǰ��ֵ��
        QNode pred = tail.getAndSet(local_node);
        // TODO �ѾɵĽڵ����ǰ���ڵ㡣
        predNode.set(pred);
        // TODO �ڵȴ�ǰ���ڵ��locked���Ϊfalse������һ�������ȴ��Ĺ���
        while (pred.locked) {
            // TODO �ó�����
            Thread.yield();
        }
    }

    /**
     * ����ǰnodeָ��ǰ��node�������������ڰѵ�ǰnode������ͷ��ɾ���������Ǳ�JVM���գ���
     *
     * lock��������Ҳ�ò�����ǰNode�������ˣ���ǰ�߳���Ҫ��unlock֮���ٴ������������Ŷ�
     *
     * ����ÿ���߳��Լ���ά��������QNode��һ�����ͷ�����ʱ��ѵ�ǰnode��Ϊǰ��node����һ����lock������ʱ�����»�ȡβnode��Ϊǰ��node��
     *
     * ������е��������ɹ̶��������̳߳�ִ�еĻ�����ῴ�����е�QNode��ʹ�û��γ�һ����������ʵ�ʲ��ǣ�
     */
    @Override
    public void unlock() {
        // unlock. ��ȡ�Լ���node�����Լ���locked����Ϊfalse��
        QNode node = localNode.get();
        if (node.lockedCnt.decrementAndGet() == 0) {
            node.locked = false;
            localNode.set(predNode.get());
        }
    }
}