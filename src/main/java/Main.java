import ziy.graphical.Home;
import ziy.pool.DaemonPool;
import ziy.pool.Pool;
import ziy.pool.daemon.DaemonManager;

/**
 * @author ziy
 * @version 1.0
 * @date 下午12:45 2020/10/25
 * @description TODO:程序启动类
 * @className Main
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //对入参进行检查
        Home home = Home.createBean("极简");
        home.build();
        //启动守护线程
        DaemonManager.startDaemon();
        //退出前
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //关闭线程池
            Pool.pool.shutdownNow();
            DaemonPool.daemonPool.shutdownNow();
        }));
    }
}

