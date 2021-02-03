package com.aurora.rpc.testserver;

import com.aurora.rpc.api.ByeObject;
import com.aurora.rpc.api.ByeService;
import com.aurora.rpc.api.HelloObject;
import com.aurora.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lc
 */
public class ByeServiceImpl implements ByeService {

    private static final Logger logger = LoggerFactory.getLogger(ByeServiceImpl.class);

    @Override
    public String bye(ByeObject object) {
        logger.info("接收到：{}", object.getMessage());
        return "这是调用的返回值，id=" + object.getId();
    }
}


