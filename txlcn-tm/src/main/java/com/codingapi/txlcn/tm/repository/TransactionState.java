package com.codingapi.txlcn.tm.repository;

/**
 * 事务状态
 * 创建事务消息后的状态为 JOIN
 * 其他事务加入后的状态为 JOIN
 * 通知事物状态时确定为 COMMIT或ROLLBACK
 * 询问事务信息时若数据为空也为ROLLBACK状态
 * @author lorne
 * @date 2020/8/7
 * @description
 */
public enum TransactionState {
    JOIN,COMMIT,ROLLBACK;

    public static TransactionState parser(boolean flag){
        if(flag){
            return COMMIT;
        }else{
            return ROLLBACK;
        }
    }
}
