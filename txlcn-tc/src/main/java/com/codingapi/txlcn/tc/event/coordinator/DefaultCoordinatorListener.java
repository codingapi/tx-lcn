package com.codingapi.txlcn.tc.event.coordinator;


 class DefaultCoordinatorListener implements CoordinatorListener{

  @Override
  public void onBeforeCreateTransaction(TransactionalEvent event) {

  }

  @Override
  public void onAfterCreateTransaction(TransactionalEvent event, Exception exp){

  }

  @Override
  public void onBeforeJoinTransaction(TransactionalEvent event) {

  }

  @Override
  public void onAfterJoinTransaction(TransactionalEvent event, Exception exp) {

  }

  @Override
  public void onBeforeNotifyTransaction(TransactionalEvent event) {

  }

  @Override
  public void onAfterNotifyTransaction(TransactionalEvent event, Exception exp) {

  }
}
