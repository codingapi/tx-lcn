package com.lorne.tx.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.lorne.core.framework.utils.KidUtils;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import com.lorne.tx.Constants;
import com.lorne.tx.service.TransactionConfirmService;
import com.lorne.tx.service.TxManagerService;
import com.lorne.tx.mq.model.TxGroup;
import com.lorne.tx.mq.model.TxInfo;
import com.lorne.tx.utils.SocketManager;
import com.lorne.tx.utils.SocketUtils;
import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by lorne on 2017/6/7.
 */
@Service
public class TxManagerServiceImpl implements TxManagerService {

    @Value("${redis_save_max_time}")
    private int redis_save_max_time;

    @Value("${transaction_netty_delay_time}")
    private int transaction_netty_delay_time;


    private final static String key_prefix = "tx_manager_default_";

    private final static String key_prefix_notify = "tx_manager_notify_";

    private final static int max_size = 100;

    private Executor threadPool = Executors.newFixedThreadPool(max_size);

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(max_size);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private TransactionConfirmService transactionConfirmService;



    private Logger logger = LoggerFactory.getLogger(TxManagerServiceImpl.class);


    @Override
    public TxGroup createTransactionGroup() {
        String groupId = KidUtils.generateShortUuid();
        TxGroup txGroup = new TxGroup();
        txGroup.setStartTime(System.currentTimeMillis());
        txGroup.setGroupId(groupId);
        String key = key_prefix + groupId;
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(key, txGroup.toJsonString(), redis_save_max_time, TimeUnit.SECONDS);
        return txGroup;
    }

    @Override
    public TxGroup addTransactionGroup(String groupId,String uniqueKey, String taskId,int isGroup, String modelName) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = key_prefix + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        TxGroup txGroup = TxGroup.parser(json);
        if (txGroup != null) {
            TxInfo txInfo = new TxInfo();
            txInfo.setModelName(modelName);
            txInfo.setKid(taskId);
            txInfo.setAddress(Constants.address);
            txInfo.setIsGroup(isGroup);
            txInfo.setUniqueKey(uniqueKey);
            txGroup.addTransactionInfo(txInfo);
            value.set(key, txGroup.toJsonString(), redis_save_max_time, TimeUnit.SECONDS);
            return txGroup;
        }
        return null;
    }

    @Override
    public  int checkTransactionGroup(String groupId, String taskId) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = key_prefix + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            key = key_prefix_notify + groupId;
            json = value.get(key);
            if (StringUtils.isEmpty(json)) {
                return 0;
            }
        }
        TxGroup txGroup = TxGroup.parser(json);

        if(txGroup.getHasOver()==0){
            return -1;
        }
        boolean res = txGroup.getState() == 1;
        if(!res) {
            return 0;
        }

        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                return info.getNotify()==0?1:0;
            }
        }

        return 0;
    }


    @Override
    public boolean checkClearGroup(String groupId, String taskId, int isGroup) {
        logger.info("checkTransactionGroup->groupId:"+groupId+",taskId:"+taskId);
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = key_prefix + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            key = key_prefix_notify + groupId;
            json = value.get(key);
            if (StringUtils.isEmpty(json)) {
                return false;
            }
        }
        TxGroup txGroup = TxGroup.parser(json);
        boolean res = txGroup.getState() == 1;

        boolean hasSet = false;
        for (TxInfo info : txGroup.getList()) {
            if (info.getKid().equals(taskId)) {
                info.setNotify(1);
                hasSet = true;
            }
        }

        if(hasSet&&res) {
            String pnKey = key_prefix_notify + groupId;
            value.set(pnKey, txGroup.toJsonString());
        }

        boolean isOver = true;
        for (TxInfo info : txGroup.getList()) {
            if(isGroup==1) {
                if (info.getIsGroup() == 0 && info.getNotify() == 0) {
                    isOver = false;
                    break;
                }
            }else{
                if (info.getNotify() == 0) {
                    isOver = false;
                    break;
                }
            }
        }

        if (isOver) {
            if(key.startsWith(key_prefix_notify)) {
                redisTemplate.delete(key);
            }
        }

        logger.info("end-checkTransactionGroup->groupId:"+groupId+",taskId:"+taskId+",res:"+res);
        return res;
    }


    @Override
    public boolean closeTransactionGroup(String groupId,int state) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String key = key_prefix + groupId;
        String json = value.get(key);
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        final TxGroup txGroup = TxGroup.parser(json);
        txGroup.setState(state);
        txGroup.setHasOver(1);
        transactionConfirmService.confirm(txGroup);
        return true;
    }


    @Override
    public void dealTxGroup(TxGroup txGroup, boolean hasOk) {
        String key = key_prefix + txGroup.getGroupId();
        if (!hasOk) {
            //未通知成功
            if (txGroup.getState() == 1) {
                ValueOperations<String, String> value = redisTemplate.opsForValue();
                String newKey = key_prefix_notify + txGroup.getGroupId();
                value.set(newKey, txGroup.toJsonString());
            }

        }
        redisTemplate.delete(key);
    }


    @Override
    public void deleteTxGroup(TxGroup txGroup) {
        String key = key_prefix + txGroup.getGroupId();
        redisTemplate.delete(key);
    }

    @Override
    public int getDelayTime() {
        return transaction_netty_delay_time;
    }


    private void awaitSend(Task task,Channel channel,String msg){
        while (!task.isAwait()&&!Thread.currentThread().interrupted()){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SocketUtils.sendMsg(channel,msg);
    }



    @Override
    public void clearNotifyData(int time) {
        Set<String> keys =  redisTemplate.keys(key_prefix_notify+"*");

        ValueOperations<String, String> value = redisTemplate.opsForValue();
        for(String key:keys){
            String json =  value.get(key);
            TxGroup txGroup = TxGroup.parser(json);
            if(txGroup==null) {
                continue;
            }

            long nowTime = System.currentTimeMillis();
            if((nowTime - txGroup.getStartTime())>time*1000*60){
                if(txGroup.getList()!=null&&txGroup.getList().size()>0){
                    for(TxInfo txInfo:txGroup.getList()){

                        String modelName = txInfo.getModelName();

                        Channel channel = SocketManager.getInstance().getChannelByModelName(modelName);

                        if(channel==null||!channel.isActive()){
                            //查找在线的有无同模块的
                            String uniqueKey = txInfo.getUniqueKey();

                            channel = SocketManager.getInstance().getChannelByUniqueKey(uniqueKey);

                            if(channel==null||!channel.isActive()){
                                continue;
                            }

                        }


                        final JSONObject jsonObject = new JSONObject();
                        jsonObject.put("a", "c");
                        jsonObject.put("g", txInfo.getKid());
                        String k = KidUtils.generateShortUuid();
                        jsonObject.put("k", k);
                        final Task task = ConditionUtils.getInstance().createTask(k);


                        ScheduledFuture future =  executorService.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                task.setBack(new IBack() {
                                    @Override
                                    public Object doing(Object... objs) throws Throwable {
                                        return "0";
                                    }
                                });
                                task.signalTask();
                            }
                        },getDelayTime(),TimeUnit.SECONDS);

                        final Channel sender = channel;

                        threadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                awaitSend(task, sender, jsonObject.toJSONString());
                            }
                        });

                        task.awaitTask();

                        if(!future.isDone()){
                            future.cancel(false);
                        }

                        try {
                            String data = (String) task.getBack().doing();
                            // 1 tx数据已经删除
                            boolean res = "1".equals(data);
                            if (res) {
                                txInfo.setNotify(1);
                            }

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        } finally {
                            task.remove();
                        }

                    }

                    boolean isOver = true;
                    for (TxInfo info : txGroup.getList()) {
                        if (info.getIsGroup()==0&&info.getNotify() == 0) {
                            isOver = false;
                            break;
                        }
                    }

                    if (isOver) {
                        redisTemplate.delete(key);
                    }
                }
            }
        }
    }


}
