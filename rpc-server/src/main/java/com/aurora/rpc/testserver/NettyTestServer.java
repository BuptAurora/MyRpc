package com.aurora.rpc.testserver;

import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.netty.server.NettyServer;
import com.aurora.rpc.registry.DefaultServiceRegistry;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.serializer.KryoSerializer;

/**
 * 测试用Netty服务提供者（服务端）
 * @author lc
 */
public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.setSerializer(new KryoSerializer());
        server.start(8888);
    }

}
