package com.codingapi.txlcn.tc.event.coordinator;

import java.util.List;


public class TransactionCoordinatorListener implements CoordinatorListener {

  private List<CoordinatorListener> listeners;

  public TransactionCoordinatorListener(
      List<CoordinatorListener> listeners) {
    this.listeners = listeners;
  }

  @Override
  public void onBeforeCreateTransaction(TransactionalEvent event) {
    if(listeners==null) {
      return;
    }
    for (CoordinatorListener listener : listeners) {
      listener.onBeforeCreateTransaction(event);
    }
  }

  @Override
  public void onAfterCreateTransaction(TransactionalEvent event, Exception exp) throws Exception {
    if(listeners==null) {
      return;
    }
    for (CoordinatorListener listener : listeners) {
      listener.onAfterCreateTransaction(event, exp);
    }
  }

  @Override
  public void onBeforeJoinTransaction(TransactionalEvent event) {
    if(listeners==null) {
      return;
    }
    for (CoordinatorListener listener : listeners) {
      listener.onBeforeJoinTransaction(event);
    }
  }

  @Override
  public void onAfterJoinTransaction(TransactionalEvent event, Exception exp) throws Exception {
    if(listeners==null) {
      return;
    }
    for (CoordinatorListener listener : listeners) {
      listener.onAfterJoinTransaction(event, exp);
    }
  }

  @Override
  public void onBeforeNotifyTransaction(TransactionalEvent event) {
    if(listeners==null) {
      return;
    }
    for (CoordinatorListener listener : listeners) {
      listener.onBeforeNotifyTransaction(event);
    }
  }

  @Override
  public void onAfterNotifyTransaction(TransactionalEvent event, Exception exp) throws Exception {
    if(listeners==null) {
      return;
    }
    for (CoordinatorListener listener : listeners) {
      listener.onAfterNotifyTransaction(event, exp);
    }
  }
}
