package com.aurora.rpc;

import com.aurora.rpc.entity.RpcRequest;
import com.aurora.rpc.serializer.CommonSerializer;

/**
 * 服务端类通用接口
 * @author lc
 */
public interface RpcServer {

    //启动服务器方法
    void start(int port);

    //自定义设置序列化器
    void setSerializer(CommonSerializer serializer);

}
