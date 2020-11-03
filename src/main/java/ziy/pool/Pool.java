package ziy.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ziy
 * @version 1.0
 * @date 9:07 2020/10/30
 * @description TODO:创建供应用使用的线程池
 * @className Pool
 */
public class Pool {
    public static final ThreadPoolExecutor pool;
    static {
        pool = new ThreadPoolExecutor(4, 4
                ,10, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(5));
    }
    private static class DefaultThreadFactory implements ThreadFactory {
        static int i = 0;
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Pool-pool-"+i);
            i++;
            return thread;
        }
    }
}
