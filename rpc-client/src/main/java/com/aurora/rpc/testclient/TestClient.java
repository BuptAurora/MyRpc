package com.aurora.rpc.testclient;

import com.aurora.rpc.api.ByeObject;
import com.aurora.rpc.api.ByeService;
import com.aurora.rpc.api.HelloObject;
import com.aurora.rpc.api.HelloService;
import com.aurora.rpc.client.RpcClientProxy;

/**
 * 测试用消费者（客户端）
 * @author lc
 */
public class TestClient {

    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
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