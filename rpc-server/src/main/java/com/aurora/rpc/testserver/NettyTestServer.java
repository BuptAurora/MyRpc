package com.aurora.rpc.testserver;

import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.transport.netty.server.NettyServer;
import com.aurora.rpc.serializer.ProtobufSerializer;

/**
 * 测试用Netty服务提供者（服务端）
 * @author lc
 */
public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1",9999);
        server.setSerializer(new ProtobufSerializer());
        server.publishService(helloService,HelloService.class);
    }
}
