package ziy.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ziy
 * @version 1.0
 * @date 上午10:18 2020/10/31
 * @description TODO:守护线程池
 * @className DemonPool
 */
public class DaemonPool {
    public static final ThreadPoolExecutor daemonPool;
    private static int i = 0;
    static {
        daemonPool = new ThreadPoolExecutor(1, 2
                , 15, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(10)
                , new DaemonThreadFactory());
    }
    private static class DaemonThreadFactory implements ThreadFactory {
        private static ThreadGroup daemonGroup = new ThreadGroup("Demon");
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(daemonGroup, r);
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setName("daemonPool-thread-"+i);
            return thread;
        }
    }
}
