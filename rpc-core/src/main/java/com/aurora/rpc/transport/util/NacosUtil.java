package com.aurora.rpc.transport.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.aurora.rpc.enumeration.RpcError;
import com.aurora.rpc.excepion.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 管理Nacos连接等工具类
 * @author lc
 */
public class NacosUtil {

    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);

    private static final NamingService namingService;
    //服务名的Set集合，避免重复
    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress address;

    private static final String SERVER_ADDR = "127.0.0.1:8848";

    static {
        namingService = getNacosNamingService();
    }

    //获取nacos命名服务
    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }
    //注册服务
    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.address = address;
        serviceNames.add(serviceName);
    }
    //获取所有已注册服务
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    //注销已经注册的服务
    public static void clearRegistry() {
        if(!serviceNames.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            //遍历删除
            Iterator<String> iterator = serviceNames.iterator();
            while(iterator.hasNext()) {
                String serviceName = iterator.next();
                try {
                    //注销服务
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    logger.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
    }
}
