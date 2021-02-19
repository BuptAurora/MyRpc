package com.aurora.rpc.testserver;

import com.aurora.rpc.RpcServer;
import com.aurora.rpc.api.ByeService;
import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.registry.DefaultServiceRegistry;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.serializer.KryoSerializer;
import com.aurora.rpc.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 * @author lc
 */

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ByeServiceImpl byeService = new ByeServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        serviceRegistry.register(byeService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.start(9000);
    }
}
