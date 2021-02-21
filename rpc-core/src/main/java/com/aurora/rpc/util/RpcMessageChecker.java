package com.aurora.rpc.util;

import com.aurora.rpc.entity.RpcRequest;
import com.aurora.rpc.entity.RpcResponse;
import com.aurora.rpc.enumeration.ResponseCode;
import com.aurora.rpc.enumeration.RpcError;
import com.aurora.rpc.excepion.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
* 检查响应与请求
* */
public class RpcMessageChecker {

    //接口名
    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    private RpcMessageChecker(){}

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse){
        //如果响应为空
        if (rpcResponse == null){
            logger.error("调用服务失败，serviceName:{}",rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME + " : "
                    + rpcRequest.getInterfaceName());
        }
        //如果请求号与响应对应的请求号不匹配
        if(!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH,INTERFACE_NAME + " : "
            + rpcRequest.getInterfaceName());
        }
        //如果响应状态码为空或不是成功状态码
        if(rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            logger.error("调用服务失败，serviceName:{},RpcResponse:{}",rpcRequest.getInterfaceName(),rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME + " : "
                    + rpcRequest.getInterfaceName());
        }
    }

}
