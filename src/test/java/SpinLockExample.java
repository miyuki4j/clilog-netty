/**
 * @author zhouhe
 * @date 2021/9/14
 */

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.zhe.lock.spin.SpinLock;
import com.zhe.lock.spin.impl.MCSReentrantSpinLock;

/**
 * http://www.cs.tau.ac.il/~shanir/nir-pubs-web/Papers/CLH.pdf https://blog.csdn.net/aesop_wubo/article/details/7533186
 * https://www.cnblogs.com/shoshana-kong/p/10831502.html
 * https://stackoverflow.com/questions/43628187/why-clh-lock-need-prev-node-in-java
 */
public class SpinLockExample {
    // private final static SpinLock lock = new CLHReentrantSpinLock();
    private final static SpinLock lock = new MCSReentrantSpinLock();
    static ConcurrentMap<Integer, String> concurrentMap = new ConcurrentHashMap<>();
    static AtomicInteger IdGen = new AtomicInteger(1);

    private static class KFC implements Runnable {
        public KFC() {}

        public void takeout() {
            long lock_nano_start_1 = 0, lock_nano_end_1 = 0;
            long unlock_nano_start_1, unlock_nano_end_1;
            long start_calc_1 = 0, start_calc_2 = 0;
            try {

                lock_nano_start_1 = System.nanoTime();
                lock.lock();
                lock_nano_end_1 = System.nanoTime();

                start_calc_1 = System.nanoTime();
                for (int i = 0; i < 10; i++) {
                    concurrentMap.put(IdGen.getAndIncrement(), UUID.randomUUID().toString());
                }
                start_calc_2 = System.nanoTime();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                unlock_nano_start_1 = System.nanoTime();
                lock.unlock();
                unlock_nano_end_1 = System.nanoTime();

                synchronized (lock) {
                    System.out
                        .println(Thread.currentThread().getName() + " lock_nano_start_1:     " + lock_nano_start_1);
                    System.out.println(Thread.currentThread().getName() + " lock_nano_end_1:       " + lock_nano_end_1);
                    System.out
                        .println(Thread.currentThread().getName() + " unlock_nano_start_1:   " + unlock_nano_start_1);
                    System.out
                        .println(Thread.currentThread().getName() + " unlock_nano_end_1:     " + unlock_nano_end_1);
                    System.out.println();
                }
            }
        }

        @Override
        public void run() {
            takeout();
        }
    }

    public static void main(String[] args) throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("KFC-T" + thread.getId());
            return thread;
        });
        KFC kfc = new KFC();

        TimeUnit.SECONDS.sleep(3);
        System.out.println(Thread.currentThread().getName() + " sleep end");
        for (int i = 1; i <= 36; i++) {
            executor.execute(kfc);
        }

        System.in.read();
    }
}
