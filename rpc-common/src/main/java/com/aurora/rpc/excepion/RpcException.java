package com.aurora.rpc.excepion;

import com.aurora.rpc.enumeration.RpcError;

/**
 * Rpc调用异常
 *
 * @author lc
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}
