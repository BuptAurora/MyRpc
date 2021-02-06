package com.aurora.rpc;

import com.aurora.rpc.entity.RpcRequest;

/**
 * 客户端类通用接口
 * @author lc
 */
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);

}
