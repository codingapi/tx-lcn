package com.codingapi.tx.dubbo.balance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance;

import java.util.List;

/**
 * create by lorne on 2017/11/29
 */
public class RoundRobinLoadBalanceProxy extends RoundRobinLoadBalance {

    private LCNBalanceProxy lcnBalanceProxy = new LCNBalanceProxy();

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        return lcnBalanceProxy.proxy(invokers,super.select(invokers, url, invocation));
    }
}
