package com.codingapi.txlcn.tm.loadbalancer;

/**
 * @author WhomHim
 * @description 事件应该执行的业务逻辑
 * @date Create in 2020-9-12 15:35:38
 */
@FunctionalInterface
public interface EventService {

    void execute() throws Exception;
}
