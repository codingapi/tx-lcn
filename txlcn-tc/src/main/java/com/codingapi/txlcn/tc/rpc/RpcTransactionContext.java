package com.codingapi.txlcn.tc.rpc;

import com.codingapi.txlcn.tc.info.TransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author lorne
 * @date 2020/7/2
 * @description
 */
@Slf4j
public class RpcTransactionContext {

    public static final String HEADER_KEY_GROUP_ID = "X-Group-ID";

    private RpcTransactionContext(){

    }

    private static RpcTransactionContext context;

    public static RpcTransactionContext getInstance() {
        if (context == null) {
            synchronized (RpcTransactionContext.class) {
                if (context == null) {
                    context = new RpcTransactionContext();
                }
            }
        }
        return context;
    }

    public void build(RpcTransactionSetter setter){
        TransactionInfo transactionInfo = TransactionInfo.current();
        setter.set(HEADER_KEY_GROUP_ID,transactionInfo.getGroupId());
    }



    public void invoke(RpcTransactionGetter rpcTransactionGetter){
        String groupId = rpcTransactionGetter.get(HEADER_KEY_GROUP_ID);
        TransactionInfo transactionInfo = TransactionInfo.current();
        if(!StringUtils.isEmpty(groupId)&&transactionInfo==null){
            new TransactionInfo(groupId);
        }
    }


    /**
     * RpcTransaction信息设置器
     */
    public interface RpcTransactionSetter {
        /**
         *
         * @param key   key
         * @param value value
         */
        void set(String key, String value);
    }

    /**
     * RpcTransaction信息获取器
     */
    public interface RpcTransactionGetter {
        /**
         *
         * @param key key
         * @return tracing value
         */
        String get(String key);
    }




}
