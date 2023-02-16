package com.codingapi.tm.manager.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.codingapi.tm.Constants;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.framework.utils.SocketUtils;
import com.codingapi.tm.manager.service.TxManagerSenderService;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.framework.utils.SocketManager;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.model.ChannelSender;
import com.codingapi.tm.redis.service.RedisServerService;
import com.lorne.core.framework.utils.KidUtils;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import com.lorne.core.framework.utils.thread.CountDownLatchHelper;
import com.lorne.core.framework.utils.thread.IExecute;
import com.codingapi.tm.netty.model.TxInfo;


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
public class TxManagerSenderServiceImpl implements TxManagerSenderService {


    private Logger logger = LoggerFactory.getLogger(TxManagerSenderServiceImpl.class);

    private ScheduledExecutorService executorService  = Executors.newScheduledThreadPool(100);

    private Executor threadPool = Executors.newFixedThreadPool(100);

    @Autowired
    private TxManagerService txManagerService;

    @Autowired
    private RedisServerService redisServerService;

    @Autowired
    private ConfigReader configReader;

    @Autowired
    private CompensateService compensateService;

    @Override
    public int confirm(TxGroup txGroup) {
        //1：从事务中获取所有参与者网络信息。然后检查网络，绑定管道对象。
        setChannel(txGroup.getList());

        //2：事务不满足直接回滚事务
        if (txGroup.getState()==0) {
            transaction(txGroup, 0);
            return 0;
        }

        if(txGroup.getRollback()==1){
            transaction(txGroup, 0);
            return -1;
        }

        //3：查看所有参与者是否执行成功。
        boolean hasOk =  transaction(txGroup, 1);
        //4：如果所有参与者执行成功，则从redis中删除事务组
        txManagerService.dealTxGroup(txGroup,hasOk);
        //5：返回结果给发起者。
        return hasOk?1:0;
    }


    /**
     * 匹配管道
     *
     * @param list
     */
    private void setChannel(List<TxInfo> list) {
        for (TxInfo info : list) {
            if(Constants.address.equals(info.getAddress())){
                //参与者记录的tx-m地址和当前tx-m地址一样，则查看管道是否存活
                Channel channel = SocketManager.getInstance().getChannelByModelName(info.getChannelAddress());
                if (channel != null &&channel.isActive()) {
                    //存活则绑定管道到参与者信息中
                    ChannelSender sender = new ChannelSender();
                    sender.setChannel(channel);
                    info.setChannel(sender);
                }
            }else{
                //不存在说明tx-m触发了变更
                ChannelSender sender = new ChannelSender();
                sender.setAddress(info.getAddress());
                sender.setModelName(info.getChannelAddress());
                info.setChannel(sender);
            }
        }
    }



    /**
     * 事务提交或回归
     *
     * @param checkSate 1-提交事务
     */
    private boolean transaction(final TxGroup txGroup, final int checkSate) {
        if (checkSate == 1) {
            logger.info("事务提交");
            //补偿请求，加载历史数据
            if (txGroup.getIsCompensate() == 1) {
                compensateService.reloadCompensate(txGroup);
            }

            CountDownLatchHelper<Boolean> countDownLatchHelper = new CountDownLatchHelper<Boolean>();
            for (final TxInfo txInfo : txGroup.getList()) {
                if (txInfo.getIsGroup() == 0) {
                    countDownLatchHelper.addExecute(new IExecute<Boolean>() {
                        @Override
                        public Boolean execute() {//channel空判断
                            if(txInfo.getChannel()==null){
                                return false;
                            }

                            final JSONObject jsonObject = new JSONObject();
                            jsonObject.put("a", "t");

                            /** 补偿请求 **/
                            if (txGroup.getIsCompensate() == 1) {
                                jsonObject.put("c", txInfo.getIsCommit());
                            } else { //正常业务
                                jsonObject.put("c", checkSate);
                            }
                            //获取参与方的任务id
                            jsonObject.put("t", txInfo.getKid());
                            final String key = KidUtils.generateShortUuid();
                            jsonObject.put("k", key);
                            //新建一个任务
                            Task task = ConditionUtils.getInstance().createTask(key);
                            //超时处理，客户端超过5秒没有返回db执行状态，则设置结果为失败
                            ScheduledFuture future = schedule(key, configReader.getTransactionNettyDelayTime());
                            //向客户端发送消息
                            threadAwaitSend(task, txInfo, jsonObject.toJSONString());
                            //阻塞等待客户端回复消息
                            task.awaitTask();
                            //超时任务还在，则取消。
                            if (!future.isDone()) {
                                future.cancel(false);
                            }

                            try {//获取参与者执行结果
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
                            } finally {//删除任务
                                task.remove();
                            }
                        }
                    });
                }
            }
            //异步获取所有参与者db执行结果
            List<Boolean> hasOks = countDownLatchHelper.execute().getData();
            //更新事务组信息
            String key = configReader.getKeyPrefix() + txGroup.getGroupId();
            redisServerService.saveTransaction(key, txGroup.toJsonString());
            //标记是否所有参与者执行成功，只要有一个失败则返回失败。
            boolean hasOk = true;
            for (boolean bl : hasOks) {
                if (!bl) {
                    hasOk = false;
                    break;
                }
            }
            logger.info("--->" + hasOk + ",group:" + txGroup.getGroupId() + ",state:" + checkSate + ",list:" + txGroup.toJsonString());
            return hasOk;
        }

        logger.info("事务回滚");
        //回滚操作只发送通过不需要等待确认
        for (TxInfo txInfo : txGroup.getList()) {
            if(txInfo.getChannel()!=null) {
                if (txInfo.getIsGroup() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("a", "t");
                    jsonObject.put("c", checkSate);
                    jsonObject.put("t", txInfo.getKid());
                    String key = KidUtils.generateShortUuid();
                    jsonObject.put("k", key);
                    txInfo.getChannel().send(jsonObject.toJSONString());
                }
            }
        }
        //删除事务组
        txManagerService.deleteTxGroup(txGroup);
        return true;

    }

    @Override
    public String sendCompensateMsg(String model, String groupId, String data,int startState) {
        JSONObject newCmd = new JSONObject();
        newCmd.put("a", "c");
        newCmd.put("d", data);
        newCmd.put("ss", startState);
        newCmd.put("g", groupId);
        newCmd.put("k", KidUtils.generateShortUuid());
        return sendMsg(model, newCmd.toJSONString(), configReader.getRedisSaveMaxTime());
    }

    @Override
    public String sendMsg(final String model,final String msg, int delay) {
        JSONObject jsonObject = JSON.parseObject(msg);
        String key = jsonObject.getString("k");

        //创建Task
        final Task task = ConditionUtils.getInstance().createTask(key);

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (!task.isAwait() && !Thread.currentThread().interrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Channel channel = SocketManager.getInstance().getChannelByModelName(model);
                if (channel != null && channel.isActive()) {
                    SocketUtils.sendMsg(channel, msg);
                }
            }
        });

        ScheduledFuture future = schedule(key, delay);

        task.awaitTask();

        if (!future.isDone()) {
            future.cancel(false);
        }

        try {
            return  (String)task.getBack().doing();
        } catch (Throwable throwable) {
            return "-1";
        }finally {
            task.remove();
        }
    }



    private void threadAwaitSend(final Task task, final TxInfo txInfo, final String msg){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (!task.isAwait() && !Thread.currentThread().interrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //判断管道是否关闭
                if(txInfo!=null&&txInfo.getChannel()!=null) {
                    //发送消息给参与者，执行db操作，参与者返回状态时会唤醒tx-m
                    txInfo.getChannel().send(msg,task);
                }else{
                    //管道不存在，则设置失败，并唤醒。
                    task.setBack(new IBack() {
                        @Override
                        public Object doing(Object... objs) throws Throwable {
                            return "-2";
                        }
                    });
                    task.signalTask();
                }
            }
        });

    }


    private ScheduledFuture schedule(final String key, int delayTime) {
        ScheduledFuture future = executorService.schedule(new Runnable() {
            @Override
            public void run() {
                Task task = ConditionUtils.getInstance().getTask(key);
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
        }, delayTime, TimeUnit.SECONDS);

        return future;
    }


}
