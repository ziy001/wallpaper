package ziy.pool.daemon;

import ziy.pool.DaemonPool;

/**
 * @author ziy
 * @version 1.0
 * @date 13:19 2020/11/1
 * @description TODO:守护线程的总管理
 * @className Manager
 */
public class DaemonManager {
    public static final MemoryManager MEMORY = new MemoryManager();

    /**
     * 启动守护线程
     */
    public static void startDaemon() {
        DaemonPool.daemonPool.submit(MEMORY);
    }
}
