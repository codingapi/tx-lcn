package com.lorne.tx.compensate.service.impl;

import com.lorne.core.framework.utils.KidUtils;
import com.lorne.tx.compensate.model.TransactionInvocation;
import com.lorne.tx.compensate.model.TransactionRecover;
import com.lorne.tx.compensate.repository.TransactionRecoverRepository;
import com.lorne.tx.compensate.service.BlockingQueueService;
import com.lorne.tx.thread.HookRunnable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lorne on 2017/7/12.
 */
@Service
public class BlockingQueueServiceImpl implements BlockingQueueService {


    private TransactionRecoverRepository recoverRepository;


    private ExecutorService threadPool = Executors.newFixedThreadPool(1);


    public BlockingQueueServiceImpl() {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("shutdown");
                threadPool.shutdown();

                while (!threadPool.isTerminated()){
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("threadPool-over");
            }
        });
    }

    @Override
    public void setTransactionRecover(TransactionRecoverRepository recoverRepository) {
        this.recoverRepository = recoverRepository;
    }

//
//    @Override
//    public String save(final TransactionInvocation transactionInvocation, final String groupId,final String taskId) {
//        final String id = KidUtils.generateShortUuid();
//        threadPool.execute(new HookRunnable() {
//            @Override
//            public void run0() {
//                TransactionRecover recover = new TransactionRecover();
//                recover.setGroupId(groupId);
//                recover.setTaskId(taskId);
//                recover.setId(id);
//                recover.setInvocation(transactionInvocation);
//                recoverRepository.create(recover);
//            }
//        });
//        return id;
//    }

    @Override
    public void save(final TransactionRecover recover) {
        threadPool.execute(new HookRunnable() {
            @Override
            public void run0() {
                recoverRepository.create(recover);
            }
        });
    }

    @Override
    public boolean delete(final String id) {
        threadPool.execute(new HookRunnable() {
            @Override
            public void run0() {
                 recoverRepository.remove(id);
            }
        });
        return true;
    }


    @Override
    public void init(String tableName, String unique) {
        recoverRepository.init(tableName, unique);
    }

}
