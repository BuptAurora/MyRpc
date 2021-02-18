package com.aurora.rpc.excepion;

/**
 * 序列化异常
 * @author lc
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
}
