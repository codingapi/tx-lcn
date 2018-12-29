package com.codingapi.tx.manager.service.rpc;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.rpc.params.NotifyGroupParams;
import com.codingapi.tx.commons.rpc.params.NotifyUnitParams;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.group.GroupRelationship;
import com.codingapi.tx.manager.group.TransUnit;
import com.codingapi.tx.manager.rpc.MessageCreator;
import com.codingapi.tx.manager.rpc.RpcExceptionHandler;
import com.codingapi.tx.manager.rpc.RpcExecuteService;
import com.codingapi.tx.manager.rpc.TransactionCmd;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import com.codingapi.tx.spi.rpc.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_notify-group")
@Slf4j
public class NotifyGroupExecuteService implements RpcExecuteService {

    private final GroupRelationship groupRelationship;

    private final RpcClient rpcClient;

    private final ProtostuffSerializer protostuffSerializer;

    private final RpcExceptionHandler rpcExceptionHandler;

    private final TxLogger txLogger;

    @Autowired
    public NotifyGroupExecuteService(GroupRelationship groupRelationship,
                                     RpcClient rpcClient, ProtostuffSerializer protostuffSerializer,
                                     RpcExceptionHandler rpcExceptionHandler, TxLogger txLogger) {
        this.groupRelationship = groupRelationship;
        this.rpcClient = rpcClient;
        this.protostuffSerializer = protostuffSerializer;
        this.rpcExceptionHandler = rpcExceptionHandler;
        this.txLogger = txLogger;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            // 解析参数
            NotifyGroupParams notifyGroupParams =
                    protostuffSerializer.deSerialize(transactionCmd.getMsg().getBytes(), NotifyGroupParams.class);

            // 保存事务组事务状态
            groupRelationship.setTransactionState(transactionCmd.getGroupId(), (short) notifyGroupParams.getState());

            log.debug("notify group params: {}", JSON.toJSONString(notifyGroupParams));

            txLogger.trace(
                    transactionCmd.getGroupId(), "",
                    Transactions.TAG_TRANSACTION, "notify group " + notifyGroupParams.getState());

            groupRelationship.setTransactionState(transactionCmd.getGroupId(),(short)notifyGroupParams.getState());
            List<TransUnit> transUnits = groupRelationship.unitsOfGroup(transactionCmd.getGroupId());
            for (TransUnit transUnit : transUnits) {
                NotifyUnitParams notifyUnitParams = new NotifyUnitParams();
                notifyUnitParams.setGroupId(transactionCmd.getGroupId());
                notifyUnitParams.setUnitId(transUnit.getUnitId());
                notifyUnitParams.setUnitType(transUnit.getUnitType());
                notifyUnitParams.setState(notifyGroupParams.getState());

                try {
                    MessageDto respMsg =
                            rpcClient.request(transUnit.getRemoteKey(), MessageCreator.notifyUnit(notifyUnitParams));
                    log.info("notify unit: {}", transUnit.getRemoteKey());

                    txLogger.trace(
                            transactionCmd.getGroupId(), notifyUnitParams.getUnitId(), Transactions.TAG_TRANSACTION,
                            "notify unit " + respMsg.getAction());

                    if (!MessageUtils.statusOk(respMsg)) {
                        // 提交/回滚失败的消息处理
                        log.warn("unit business exception.");
                        rpcExceptionHandler.handleNotifyUnitBusinessException(
                                Arrays.asList(notifyUnitParams, transactionCmd.getRemoteKey()),
                                protostuffSerializer.deSerialize(respMsg.getBytes(), Throwable.class));
                    }


                } catch (RpcException e) {
                    txLogger.trace(transactionCmd.getGroupId(), "", "manager", "notify unit exception");
                    // 提交/回滚通讯失败
                    log.warn("unit message exception.");
                    rpcExceptionHandler.handleNotifyUnitMessageException(
                            Arrays.asList(notifyUnitParams, transactionCmd.getRemoteKey()), e);
                }
            }
        } catch (SerializerException e) {
            throw new TxManagerException(e.getMessage());
        } finally {
            groupRelationship.removeGroup(transactionCmd.getGroupId());
            txLogger.trace(transactionCmd.getGroupId(), "", Transactions.TAG_TRANSACTION, "notify group over");
        }
        return null;
    }
}
