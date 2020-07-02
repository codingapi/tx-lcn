package com.codingapi.txlcn.tc.event.transaction;

import com.codingapi.txlcn.tc.info.TransactionInfo;

import java.util.List;


public class TransactionEventContext implements TransactionEventListener {

  private List<TransactionEventListener> transactionEventListeners;

  public TransactionEventContext(
      List<TransactionEventListener> transactionEventListeners) {
    this.transactionEventListeners = transactionEventListeners;
  }

  @Override
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
