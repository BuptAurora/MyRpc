package com.aurora.rpc;

import com.aurora.rpc.entity.RpcRequest;
import com.aurora.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 * @author lc
 */
public interface RpcClient {

    //发送请求
    Object sendRequest(RpcRequest rpcRequest);

    //自定义设置序列化器
    void setSerializer(CommonSerializer serializer);
}
