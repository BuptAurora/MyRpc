package com.aurora.rpc.testserver;

import com.aurora.rpc.api.ByeService;
import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.registry.DefaultServiceRegistry;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.server.RpcServer;

/**
 * 测试用服务提供方（服务端）
 * @author lc
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ByeService byeService = new ByeServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        serviceRegistry.register(byeService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
