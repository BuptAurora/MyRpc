package com.aurora.rpc.testclient;

import com.aurora.rpc.transport.RpcClientProxy;
import com.aurora.rpc.api.HelloObject;
import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.serializer.HessianSerializer;
import com.aurora.rpc.transport.socket.client.SocketClient;

public class SocketTestClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.setSerializer(new HessianSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);

        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(12, "This is a hello message");
        ByeService byeService = proxy.getProxy(ByeService.class);
        ByeObject byeObject = new ByeObject(16, "This is a bye message");

        String res1 = helloService.hello(helloObject);
        String res2 = byeService.bye(byeObject);
        System.out.println(res1);
        System.out.println(res2);
    }
}
