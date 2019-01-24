package com.codingapi.txlcn.spi.message.listener;

/**
 * Description:
 * Date: 19-1-24 下午6:02
 *
 * @author ujued
 */
public interface TCRegisterListener {

    void onRegister(String modId);

    void onCancel(String modId);
}
