package com.aurora.rpc.testserver;

import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.server.RpcServer;

/**
 * 测试用服务提供方（服务端）
 * @author lc
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }

}
