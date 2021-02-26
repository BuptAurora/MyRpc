package com.aurora.rpc.transport.socket.server;

import com.aurora.rpc.handler.RequestHandler;
import com.aurora.rpc.entity.RpcRequest;
import com.aurora.rpc.entity.RpcResponse;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.serializer.CommonSerializer;
import com.aurora.rpc.transport.util.ObjectReader;
import com.aurora.rpc.transport.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 处理RpcRequest的工作线程
 * @author lc
 */
public class RequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;


    public RequestHandlerThread(Socket socket, RequestHandler requestHandler,CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result,rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

}
