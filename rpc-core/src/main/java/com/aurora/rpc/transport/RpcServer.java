package com.aurora.rpc.transport;

import com.aurora.rpc.serializer.CommonSerializer;

/**
 * 服务端类通用接口
 * @author lc
 */
public interface RpcServer {

    //启动服务器方法
    void start();

    //自定义设置序列化器
    void setSerializer(CommonSerializer serializer);

    //向Nacos注册服务
    <T> void publishService(Object service,Class<T> serviceClass);

}
