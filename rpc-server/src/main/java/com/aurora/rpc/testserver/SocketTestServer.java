package com.aurora.rpc.testserver;

import com.aurora.rpc.RpcServer;
import com.aurora.rpc.api.ByeService;
import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.serializer.KryoSerializer;
import com.aurora.rpc.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方（服务端）
 * @author lc
 */

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        SocketServer socketServer = new SocketServer("127.0.0.1",9998);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.publishService(helloService,HelloService.class);
    }
}
