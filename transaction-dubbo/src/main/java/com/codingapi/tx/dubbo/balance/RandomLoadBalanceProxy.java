package com.codingapi.tx.dubbo.balance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;

import java.util.List;

/**
 * create by lorne on 2017/11/29
 */
public class RandomLoadBalanceProxy extends RandomLoadBalance {

    private LCNBalanceProxy lcnBalanceProxy = new LCNBalanceProxy();

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        return lcnBalanceProxy.proxy(invokers,super.select(invokers, url, invocation));
    }
}
