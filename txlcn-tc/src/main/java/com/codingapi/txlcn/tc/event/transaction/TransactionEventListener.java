package com.codingapi.txlcn.tc.event.transaction;

import com.codingapi.txlcn.tc.info.TransactionInfo;

/**
 * 事务协调监听,TC与TM的通讯步骤监听.
 */
public interface TransactionEventListener {


  /**
   * 创建事务之前
   *
   * @param transactionInfo 事务消息
   */
  void onBeforeCreateTransaction(TransactionInfo transactionInfo);

  /**
   * 创建事务之后
   *
   * @param transactionInfo 事务消息
   */
  void onAfterCreateTransaction(TransactionInfo transactionInfo);


  /**
   * 加入事务之前
   *
   * @param transactionInfo 事务消息
   */
  void onBeforeJoinTransaction(TransactionInfo transactionInfo);

  /**
   * 加入事务之后
   *
   * @param transactionInfo 事务消息
   */
  void onAfterJoinTransaction(TransactionInfo transactionInfo);


  /**
   * 通知事务之前
   *
   * @param transactionInfo 事务消息
   */
  void onBeforeNotifyTransaction(TransactionInfo transactionInfo);


  /**
   * 通知事务之后
   *
   * @param transactionInfo 事务消息
   */
  void onAfterNotifyTransaction(TransactionInfo transactionInfo);


}
