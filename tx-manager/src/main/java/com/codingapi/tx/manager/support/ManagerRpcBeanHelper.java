package com.codingapi.tx.manager.support;


import com.codingapi.tx.client.springcloud.spi.message.LCNCmdType;
import com.codingapi.tx.manager.support.message.RpcExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2018/12/2
 * @description BeanName 获取工具类
 */
@Component
public class ManagerRpcBeanHelper {


    /**
     * manager bean 名称格式
     * manager_%s_%s
     * manager:前缀 %s:业务处理(create,add,close)
     */
    private static final String RPC_BEAN_NAME_FORMAT = "rpc_%s";


    @Autowired
    private ApplicationContext spring;


    public String getServiceBeanName(LCNCmdType cmdType) {
        return String.format(RPC_BEAN_NAME_FORMAT, cmdType.getCode());
    }


    public RpcExecuteService loadManagerService(LCNCmdType cmdType) {
        return spring.getBean(getServiceBeanName(cmdType), RpcExecuteService.class);
    }

    public <T> T getByType(Class<T> type) {
        return spring.getBean(type);
    }
}
