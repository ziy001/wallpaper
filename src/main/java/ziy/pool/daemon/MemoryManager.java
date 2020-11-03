package ziy.pool.daemon;

import java.util.concurrent.TimeUnit;

/**
 * @author ziy
 * @version 1.0
 * @date 12:46 2020/11/1
 * @description TODO:
 * @className MemoryManager
 */
public class MemoryManager implements Runnable {
    private static final int THRESHOLD = 150;
    private static final int time = 30;
    @Override
    public void run() {
        System.out.println("内存监视开启");
        //实时获取JVM的内存情况
        Runtime runtime = Runtime.getRuntime();
        //监视
        while (runtime.totalMemory()/(1024*2) - runtime.freeMemory()/(1024*2) > THRESHOLD) {
            System.out.println("运行内存清理");
            runtime.gc();
            runtime.runFinalization();
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("内存监视关闭");
    }
}
