package com.codingapi.tx.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.compensate.service.CompensateService;
import com.codingapi.tx.service.DiscoveryService;
import com.codingapi.tx.service.TxManagerService;
import com.codingapi.tx.service.model.TxServer;
import com.codingapi.tx.service.model.TxState;
import com.codingapi.tx.utils.SocketUtils;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import com.codingapi.tx.Constants;
import com.codingapi.tx.compensate.model.TransactionCompensateMsg;
import com.codingapi.tx.service.TxService;
import com.codingapi.tx.utils.SocketManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.eureka.EurekaServerContextHolder;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lorne on 2017/7/1.
 */
@Service
public class TxServiceImpl implements TxService {

    @Value("${redis_save_max_time}")
    private int redis_save_max_time;

    @Value("${transaction_netty_heart_time}")
    private int transaction_netty_heart_time;

    @Value("${transaction_netty_delay_time}")
    private int transaction_netty_delay_time;


    @Autowired
    private TxManagerService managerService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryService discoveryService;

    @Autowired
    private TxManagerService txManagerService;

    @Autowired
    private CompensateService compensateService;


    @Override
    public TxServer getServer() {
        List<String> urls= getServices();
        List<TxState> states = new ArrayList<>();
        for(String url:urls){
            TxState state = restTemplate.getForObject(url+"/tx/manager/state",TxState.class);
            states.add(state);
        }
        if(states.size()<=1) {
            TxState state = getState();
            if (state.getMaxConnection() > state.getNowConnection()) {
                return TxServer.format(state);
            } else {
                return null;
            }
        }else{
            //找默认数据
            TxState state = getDefault(states,0);
            if (state == null) {
                //没有满足的默认数据
                return null;
            }
            return TxServer.format(state);
        }
    }

    private TxState getDefault(List<TxState> states, int index) {
        TxState state = states.get(index);
        if (state.getMaxConnection() == state.getNowConnection()) {
            index++;
            if (states.size() - 1 >= index) {
                return getDefault(states, index);
            } else {
                return null;
            }
        } else {
            return state;
        }
    }


    @Override
    public TxState getState() {
        TxState state = new TxState();
        String ipAddress = EurekaServerContextHolder.getInstance().getServerContext().getApplicationInfoManager().getEurekaInstanceConfig().getIpAddress();
        if(!isIp(ipAddress)){
            ipAddress = "127.0.0.1";
        }
        state.setIp(ipAddress);
        state.setPort(Constants.socketPort);
        state.setMaxConnection(SocketManager.getInstance().getMaxConnection());
        state.setNowConnection(SocketManager.getInstance().getNowConnection());
        state.setRedisSaveMaxTime(redis_save_max_time);
        state.setTransactionNettyDelayTime(transaction_netty_delay_time);
        state.setTransactionNettyHeartTime(transaction_netty_heart_time);
        state.setSlbList(getServices());
        return state;
    }

    public static boolean isIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }




    private List<String> getServices(){
        List<String> urls = new ArrayList<>();
        List<InstanceInfo> instanceInfos =discoveryService.getConfigServiceInstances();
        for (InstanceInfo instanceInfo : instanceInfos) {
            String url = instanceInfo.getHomePageUrl();
            String address = instanceInfo.getIPAddr();
            if (isIp(address)) {
                urls.add(url);
            }else{
                url = url.replace(address,"127.0.0.1");
                urls.add(url);
            }
        }
        return urls;
    }


    @Override
    public boolean clearTransaction(String groupId, String taskId, int isGroup) {
        return managerService.clearTransaction(groupId,taskId,isGroup);
    }

    @Override
    public int getTransaction(String groupId, String taskId) {
        return managerService.getTransaction(groupId, taskId);
    }

    @Override
    public boolean sendCompensateMsg(String groupId, String model, String uniqueKey, String className, String method, String data, int time) {
        TransactionCompensateMsg transactionCompensateMsg = new TransactionCompensateMsg(groupId,model,uniqueKey,className,method,data,time);
        return compensateService.saveCompensateMsg(transactionCompensateMsg);
    }

    @Override
    public String sendMsg(String model,String msg) {
        JSONObject jsonObject = JSON.parseObject(msg);
        String key = jsonObject.getString("k");

        //创建Task
        final Task task = ConditionUtils.getInstance().createTask(key);

        new Thread(new Runnable() {
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
                if (channel != null &&channel.isActive()) {
                    SocketUtils.sendMsg(channel,msg);
                }
            }
        }).start();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
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
        }, txManagerService.getDelayTime() * 1000);


        task.awaitTask();
        timer.cancel();


        try {
            return  (String)task.getBack().doing();
        } catch (Throwable throwable) {
            return "-1";
        }finally {
            task.remove();
        }
    }
}
