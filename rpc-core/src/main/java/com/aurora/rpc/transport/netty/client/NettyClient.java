package com.aurora.rpc.transport.netty.client;

import com.aurora.rpc.registry.NacosServiceDiscovery;
import com.aurora.rpc.registry.NacosServiceRegistry;
import com.aurora.rpc.registry.ServiceDiscovery;
import com.aurora.rpc.registry.ServiceRegistry;
import com.aurora.rpc.transport.RpcClient;
import com.aurora.rpc.entity.RpcRequest;
import com.aurora.rpc.entity.RpcResponse;
import com.aurora.rpc.enumeration.RpcError;
import com.aurora.rpc.excepion.RpcException;
import com.aurora.rpc.serializer.CommonSerializer;
import com.aurora.rpc.transport.util.RpcMessageChecker;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NIO方式消费侧客户端类
 * @author lc
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

//    private String host;
//    private int port;
//    private static final Bootstrap bootstrap;
    private final ServiceDiscovery serviceDiscovery;


    private CommonSerializer serializer;

    public NettyClient(){
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

//    public NettyClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

//    static {
//        EventLoopGroup group = new NioEventLoopGroup();
//        bootstrap = new Bootstrap();
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_KEEPALIVE,true);
//    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {

        if (serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

//        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel ch) {
//                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast(new CommonDecoder())
//                        .addLast(new CommonEncoder(serializer))
//                        .addLast(new NettyClientHandler());
//            }
//        });


        //原子性变量引用，保证result线程安全
        AtomicReference<Object> result = new AtomicReference<>(null);

        try {
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            logger.info("客户端连接到服务器 {}:{}", host, port);
//            Channel channel = future.channel();

//            Channel channel = ChannelProvider.get(new InetSocketAddress(host, port), serializer);

            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);

            if(channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest,rpcResponse);
                result.set(rpcResponse.getData());
            }else{
                channel.close();
                System.exit(0);
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生: ", e);
            Thread.currentThread().interrupt();
        }
        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }


}
