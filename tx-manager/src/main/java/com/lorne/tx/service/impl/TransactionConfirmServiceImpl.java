package com.lorne.tx.service.impl;


import com.alibaba.fastjson.JSONObject;

import com.lorne.core.framework.utils.KidUtils;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import com.lorne.core.framework.utils.thread.CountDownLatchHelper;
import com.lorne.core.framework.utils.thread.IExecute;
import com.lorne.tx.Constants;
import com.lorne.tx.mq.model.TxGroup;
import com.lorne.tx.mq.model.TxInfo;
import com.lorne.tx.service.TransactionConfirmService;
import com.lorne.tx.service.TxManagerService;
import com.lorne.tx.service.model.ChannelSender;
import com.lorne.tx.utils.SocketManager;


import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;


/**
 * Created by lorne on 2017/6/9.
 */
@Service
public class TransactionConfirmServiceImpl implements TransactionConfirmService {


    private Logger logger = LoggerFactory.getLogger(TransactionConfirmServiceImpl.class);

    private ScheduledExecutorService executorService  = Executors.newScheduledThreadPool(100);

    private Executor threadPool = Executors.newFixedThreadPool(100);

    @Autowired
    private TxManagerService txManagerService;

    @Override
    public void confirm(TxGroup txGroup) {
        //绑定管道对象，检查网络
        setChannel(txGroup.getList());

        //事务不满足直接回滚事务
        if (txGroup.getState()==0) {
            transaction(txGroup, 0);
            return;
        }

        boolean hasOk =  transaction(txGroup, 1);
        txManagerService.dealTxGroup(txGroup,hasOk);
    }


    /**
     * 匹配管道
     *
     * @param list
     */
    private void setChannel(List<TxInfo> list) {
        for (TxInfo info : list) {
            if(Constants.address.equals(info.getAddress())){
                Channel channel = SocketManager.getInstance().getChannelByModelName(info.getModelName());
                if (channel != null &&channel.isActive()) {
                    ChannelSender sender = new ChannelSender();
                    sender.setChannel(channel);

                    info.setChannel(sender);
                }
            }else{
                ChannelSender sender = new ChannelSender();
                sender.setAddress(info.getAddress());
                sender.setModelName(info.getModelName());

                info.setChannel(sender);
            }
        }
    }


    private void awaitSend(Task task, TxInfo txInfo,String msg){
            while (!task.isAwait() && !Thread.currentThread().interrupted()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(txInfo.getChannel()!=null) {
                txInfo.getChannel().send(msg);
            }else{
                task.setBack(new IBack() {
                    @Override
                    public Object doing(Object... objs) throws Throwable {
                        return "-2";
                    }
                });
                task.signalTask();
            }
    }

    /**
     * 事务提交或回归
     *
     * @param checkSate
     */
    private boolean transaction(TxGroup txGroup, final int checkSate) {
        if (checkSate == 1) {
            CountDownLatchHelper<Boolean> countDownLatchHelper = new CountDownLatchHelper<>();
            for (final TxInfo txInfo : txGroup.getList()) {
                if (txInfo.getIsGroup() == 0) {
                    countDownLatchHelper.addExecute(new IExecute<Boolean>() {
                        @Override
                        public Boolean execute() {
                            if(txInfo.getChannel()==null){
                                return false;
                            }
                            final JSONObject jsonObject = new JSONObject();
                            jsonObject.put("a", "t");
                            jsonObject.put("c", checkSate);
                            jsonObject.put("t", txInfo.getKid());
                            final String key = KidUtils.generateShortUuid();
                            jsonObject.put("k", key);
                            Task task = ConditionUtils.getInstance().createTask(key);

                            ScheduledFuture future = executorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    Task task = ConditionUtils.getInstance().createTask(key);
                                    if(task!=null&&!task.isNotify()) {
                                        task.setBack(new IBack() {
                                            @Override
                                            public Object doing(Object... objs) throws Throwable {
                                                return "-2";
                                            }
                                        });
                                        task.signalTask();
                                    }
                                }
                            }, txManagerService.getDelayTime(), TimeUnit.SECONDS);

                            threadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    awaitSend(task, txInfo, jsonObject.toJSONString());
                                }
                            });
                            task.awaitTask();

                            if (!future.isDone()) {
                                future.cancel(false);
                            }


                            try {
                                String data = (String) task.getBack().doing();
                                // 1  成功 0 失败 -1 task为空 -2 超过
                                boolean res = "1".equals(data);

                                if (res) {
                                    txInfo.setNotify(1);
                                }

                                return res;
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                return false;
                            } finally {
                                task.remove();
                            }
                        }
                    });
                }
            }

            List<Boolean> hasOks = countDownLatchHelper.execute().getData();

            boolean hasOk = true;
            for (boolean bl : hasOks) {
                if (bl == false) {
                    hasOk = false;
                    break;
                }
            }
            logger.info("--->" + hasOk + ",group:" + txGroup.getGroupId() + ",state:" + checkSate + ",list:" + txGroup.toJsonString());
            return hasOk;
        }else{
            //回滚操作只发送通过不需要等待确认
            for (TxInfo txInfo : txGroup.getList()) {
                if(txInfo.getChannel()!=null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("a", "t");
                    jsonObject.put("c", checkSate);
                    jsonObject.put("t", txInfo.getKid());
                    String key = KidUtils.generateShortUuid();
                    jsonObject.put("k", key);
                    txInfo.getChannel().send(jsonObject.toJSONString());
                }
            }
            txManagerService.deleteTxGroup(txGroup);
            return true;
        }

    }


}
