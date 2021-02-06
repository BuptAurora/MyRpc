package com.aurora.rpc;

import com.aurora.rpc.entity.RpcRequest;

/**
 * 服务端类通用接口
 * @author lc
 */
public interface RpcServer {

    void start(int port);
}
