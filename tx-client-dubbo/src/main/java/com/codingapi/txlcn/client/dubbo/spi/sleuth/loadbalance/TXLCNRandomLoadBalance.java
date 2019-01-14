package com.codingapi.txlcn.client.dubbo.spi.sleuth.loadbalance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
public class TXLCNRandomLoadBalance extends RandomLoadBalance {


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        return TXLCNLoadBalance.chooseInvoker(invokers, url, invocation, this::loadSelect);
    }

    public <T> Invoker<T> loadSelect(List<Invoker<T>> invokers, URL url, Invocation invocation){
        return super.select(invokers, url, invocation);
    }

}
