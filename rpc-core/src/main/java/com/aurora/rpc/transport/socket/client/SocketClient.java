package com.aurora.rpc.transport.socket.client;

import com.aurora.rpc.registry.NacosServiceDiscovery;
import com.aurora.rpc.registry.NacosServiceRegistry;
import com.aurora.rpc.registry.ServiceDiscovery;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.transport.RpcClient;
import com.aurora.rpc.entity.RpcRequest;
import com.aurora.rpc.entity.RpcResponse;
import com.aurora.rpc.enumeration.ResponseCode;
import com.aurora.rpc.enumeration.RpcError;
import com.aurora.rpc.excepion.RpcException;
import com.aurora.rpc.serializer.CommonSerializer;
import com.aurora.rpc.transport.util.ObjectReader;
import com.aurora.rpc.transport.util.ObjectWriter;
import com.aurora.rpc.transport.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Socket方式远程方法调用的消费者（客户端）
 *
 * @author lc
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

//    private final String host;
//    private final int port;

    private final ServiceDiscovery serviceDiscovery;


    private CommonSerializer serializer;


//    public SocketClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    public SocketClient(){
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            Object object = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) object;

            if(rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }

            RpcMessageChecker.check(rpcRequest,rpcResponse);
            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
