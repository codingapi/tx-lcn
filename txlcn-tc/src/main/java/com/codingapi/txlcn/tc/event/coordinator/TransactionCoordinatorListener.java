package com.codingapi.txlcn.tc.event.coordinator;

import com.codingapi.txlcn.tc.info.TransactionInfo;

import java.util.List;


public class TransactionCoordinatorListener implements CoordinatorListener {

  private List<CoordinatorListener> listeners;

  public TransactionCoordinatorListener(
      List<CoordinatorListener> listeners) {
    this.listeners = listeners;
  }

  @Override
  public void onBeforeCreateTransaction(TransactionInfo transactionInfo) {
    for (CoordinatorListener listener : listeners) {
      listener.onBeforeCreateTransaction(transactionInfo);
    }
  }

  @Override
  public void onAfterCreateTransaction(TransactionInfo transactionInfo) {
    for (CoordinatorListener listener : listeners) {
      listener.onAfterCreateTransaction(transactionInfo);
    }
  }

  @Override
  public void onBeforeJoinTransaction(TransactionInfo transactionInfo) {
    for (CoordinatorListener listener : listeners) {
      listener.onBeforeJoinTransaction(transactionInfo);
    }
  }

  @Override
  public void onAfterJoinTransaction(TransactionInfo transactionInfo)  {
    for (CoordinatorListener listener : listeners) {
      listener.onAfterJoinTransaction(transactionInfo);
    }
  }

  @Override
  public void onBeforeNotifyTransaction(TransactionInfo transactionInfo) {
    for (CoordinatorListener listener : listeners) {
      listener.onBeforeNotifyTransaction(transactionInfo);
    }
  }

  @Override
  public void onAfterNotifyTransaction(TransactionInfo transactionInfo) {
    for (CoordinatorListener listener : listeners) {
      listener.onAfterNotifyTransaction(transactionInfo);
    }
  }
}
