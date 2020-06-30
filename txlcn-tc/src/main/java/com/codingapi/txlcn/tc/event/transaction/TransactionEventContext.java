package com.codingapi.txlcn.tc.event.transaction;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.info.TransactionInfo;

import java.util.List;


@Model(flag = "C",value = "事务事件监听处理",color = "#FF88EE")
public class TransactionEventContext implements TransactionEventListener {

  @GraphRelation(value = "*-->",type = TransactionEventListener.class)
  private List<TransactionEventListener> transactionEventListeners;

  public TransactionEventContext(
      List<TransactionEventListener> transactionEventListeners) {
    this.transactionEventListeners = transactionEventListeners;
  }

  @Override
  @GraphRelation(value = "..>",type = TransactionInfo.class)
  public void onBeforeCreateTransaction(TransactionInfo transactionInfo) {
    for (TransactionEventListener listener : transactionEventListeners) {
      listener.onBeforeCreateTransaction(transactionInfo);
    }
  }

  @Override
  public void onAfterCreateTransaction(TransactionInfo transactionInfo) {
    for (TransactionEventListener listener : transactionEventListeners) {
      listener.onAfterCreateTransaction(transactionInfo);
    }
  }

  @Override
  public void onBeforeJoinTransaction(TransactionInfo transactionInfo) {
    for (TransactionEventListener listener : transactionEventListeners) {
      listener.onBeforeJoinTransaction(transactionInfo);
    }
  }

  @Override
  public void onAfterJoinTransaction(TransactionInfo transactionInfo)  {
    for (TransactionEventListener listener : transactionEventListeners) {
      listener.onAfterJoinTransaction(transactionInfo);
    }
  }

  @Override
  public void onBeforeNotifyTransaction(TransactionInfo transactionInfo) {
    for (TransactionEventListener listener : transactionEventListeners) {
      listener.onBeforeNotifyTransaction(transactionInfo);
    }
  }

  @Override
  public void onAfterNotifyTransaction(TransactionInfo transactionInfo) {
    for (TransactionEventListener listener : transactionEventListeners) {
      listener.onAfterNotifyTransaction(transactionInfo);
    }
  }

}
