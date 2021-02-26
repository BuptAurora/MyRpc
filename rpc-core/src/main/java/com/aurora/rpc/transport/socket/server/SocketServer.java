package com.aurora.rpc.transport.socket.server;

import com.aurora.rpc.handler.RequestHandler;
import com.aurora.rpc.hook.ShutdownHook;
import com.aurora.rpc.provider.ServiceProvider;
import com.aurora.rpc.registry.NacosServiceRegistry;
import com.aurora.rpc.provider.ServiceProviderImpl;
import com.aurora.rpc.transport.RpcServer;
import com.aurora.rpc.enumeration.RpcError;
import com.aurora.rpc.excepion.RpcException;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.serializer.CommonSerializer;
import com.aurora.rpc.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket方式远程方法调用的提供者（服务端）
 * @author lc
 */
public class SocketServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

//    private static final int CORE_POOL_SIZE = 5;
//    private static final int MAXIMUM_POOL_SIZE = 50;
//    private static final int KEEP_ALIVE_TIME = 60;
//    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private final String host;
    private final int port;

    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
//    private final ServiceRegistry serviceRegistry;

    private CommonSerializer serializer;


    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketServer(String host,int port) {
//        this.serviceRegistry = serviceRegistry;
//        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);

        this.host = host;
        this.port = port;
        //使用线程池工厂创建线程池
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }
    @Override
    public <T> void publishService(T service,Class<T> serviceClass) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service,serviceClass);
        serviceRegistry.register(serviceClass.getCanonicalName(),new InetSocketAddress(host,port));
        start();
    }
    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host,port));
            logger.info("服务器启动...");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler,serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);

        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
