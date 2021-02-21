package com.aurora.rpc.testclient;

import com.aurora.rpc.RpcClient;
import com.aurora.rpc.RpcClientProxy;
import com.aurora.rpc.api.HelloObject;
import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.netty.client.NettyClient;
import com.aurora.rpc.serializer.ProtobufSerializer;

/**
 * 测试用Netty消费者
 * @author lc
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 8888);
        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

    }

}
