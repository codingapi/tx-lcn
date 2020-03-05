package com.codingapi.txlcn.tc.event.coordinator;

/**
 * 事务协调监听,TC与TM的通讯步骤监听.
 */
public interface CoordinatorListener {


  /**
   * 创建事务之前
   *
   * @param event 事务消息
   */
  void onBeforeCreateTransaction(TransactionalEvent event);

  /**
   * 创建事务之后
   */
  void onAfterCreateTransaction(TransactionalEvent event, Exception exp);


  /**
   * 加入事务之前
   *
   * @param event 事务消息
   */
  void onBeforeJoinTransaction(TransactionalEvent event);

  /**
   * 加入事务之后
   *
   * @param event 事务消息
   */
  void onAfterJoinTransaction(TransactionalEvent event, Exception exp);


  /**
   * 通知事务之前
   *
   * @param event 事务消息
   */
  void onBeforeNotifyTransaction(TransactionalEvent event);


  /**
   * 通知事务之后
   *
   * @param event 事务消息
   */
  void onAfterNotifyTransaction(TransactionalEvent event, Exception exp);


}
