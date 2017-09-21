package com.lorne.tx.service.impl;

import com.lorne.tx.service.JobService;
import com.lorne.tx.service.TxManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/8/8
 */
@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private TxManagerService txManagerService;

    @Override
    public void clearNotifyData() {

        new Thread(){
            @Override
            public void run() {

                int time = 5;
                while(true){
                    try {
                        Thread.sleep(1000*60*time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    txManagerService.clearNotifyData(time);
                }
            }
        }.start();

    }
}
