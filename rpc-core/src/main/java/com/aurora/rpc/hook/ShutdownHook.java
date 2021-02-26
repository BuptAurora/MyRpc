package com.aurora.rpc.hook;

import com.aurora.rpc.factory.ThreadPoolFactory;
import com.aurora.rpc.transport.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * 钩子类
 * 钩子是什么呢？是在某些事件发生后自动去调用的方法。
 * 把注销服务的方法写到关闭系统的钩子方法里
 * @author lc
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");
    //单例模式创建关闭系统的钩子对象
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }

}
