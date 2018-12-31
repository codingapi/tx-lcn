package com.codingapi.tx.client.support.checking;

import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.client.support.LCNTransactionBeanHelper;
import com.codingapi.tx.client.aspectlog.ThreadPoolLogger;
import com.codingapi.tx.client.support.rpc.MessageCreator;
import com.codingapi.tx.client.support.rpc.TxMangerReporter;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.rpc.params.TxExceptionParams;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Description: 基于JDK任务调度线程池实现的DTX检测
 * Date: 2018/12/19
 *
 * @author ujued
 * @see DTXChecking
 */
@Component
@Slf4j
public class SimpleDTXChecking implements DTXChecking {

    private static final Map<String, ScheduledFuture> delayTasks = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    static {
        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduledExecutorService.shutdown();
            try {
                scheduledExecutorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
    }

    private final RpcClient rpcClient;

    private final ProtostuffSerializer protostuffSerializer;

    private final TxClientConfig clientConfig;

    private final LCNTransactionBeanHelper transactionBeanHelper;

    private final TxLogger txLogger;

    private final ThreadPoolLogger aspectLogger;

    @Autowired
    private TxMangerReporter txMangerReporter;

    @Autowired
    public SimpleDTXChecking(RpcClient rpcClient, ProtostuffSerializer protostuffSerializer,
                             TxClientConfig clientConfig, LCNTransactionBeanHelper transactionBeanHelper,
                             ThreadPoolLogger aspectLogger, TxLogger txLogger) {
        this.rpcClient = rpcClient;
        this.protostuffSerializer = protostuffSerializer;
        this.clientConfig = clientConfig;
        this.transactionBeanHelper = transactionBeanHelper;
        this.aspectLogger = aspectLogger;
        this.txLogger = txLogger;
    }

    @Override
    public void startDelayCheckingAsync(String groupId, String unitId, String transactionType) {
        txLogger.trace(groupId, unitId, Transactions.TAG_TASK, "start delay checking task");
        ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(() -> {
            try {
                String channel = rpcClient.loadRemoteKey();
                MessageDto messageDto = rpcClient.request(channel, MessageCreator.askTransactionState(groupId, unitId));
                int state = protostuffSerializer.deSerialize(messageDto.getBytes(), Short.class);
                log.info("support > ask transaction state:{}", state);
                txLogger.trace(groupId, unitId, Transactions.TAG_TASK, "ask transaction state " + state);
                if (state == -1) {
                    log.error("delay clean transaction error.");
                    onAskTransactionStateException(groupId, unitId, transactionType);
                } else {
                    /*
                     @see {link #TransactionCleanTemplate}
                     */
                    transactionBeanHelper.loadTransactionCleanService(transactionType).clear(groupId, state, unitId, transactionType);
                    aspectLogger.clearLog(groupId, unitId);
                }

            } catch (RpcException e) {
                onAskTransactionStateException(groupId, unitId, transactionType);
            } catch (TransactionClearException | SerializerException e) {
                log.error("{} > [transaction state message] error or [clean transaction] error.", transactionType);
            }
        }, clientConfig.getDtxTime(), TimeUnit.MILLISECONDS);
        delayTasks.put(groupId + unitId, scheduledFuture);
    }

    @Override
    public void stopDelayChecking(String groupId, String unitId) {
        ScheduledFuture scheduledFuture = delayTasks.get(groupId + unitId);
        if (Objects.nonNull(scheduledFuture)) {
            txLogger.trace(groupId, unitId, Transactions.TAG_TASK, "stop delay checking task");
            log.info("cancel {}:{} checking.", groupId, unitId);
            scheduledFuture.cancel(true);
        }
    }

    private void onAskTransactionStateException(String groupId, String unitId, String transactionType) {
        try {
            // 通知TxManager事务补偿
            txMangerReporter.reportTransactionState(groupId, unitId, TxExceptionParams.ASK_ERROR, 0);
            log.warn("{} > has compensation info!", transactionType);

            // 事务回滚, 保留适当的补偿信息
            transactionBeanHelper.loadTransactionCleanService(transactionType).clear(groupId, 0, unitId, transactionType);
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error.", transactionType);
        }
    }
}
