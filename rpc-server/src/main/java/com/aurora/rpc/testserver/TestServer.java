package com.aurora.rpc.testclient.testserver;

import com.aurora.rpc.testclient.api.HelloService;
import com.aurora.rpc.testclient.server.RpcServer;

/**
 * 测试用服务提供方（服务端）
 * @author ziyang
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }

}
