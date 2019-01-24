package com.codingapi.txlcn.spi.message.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Date: 19-1-24 下午6:04
 *
 * @author ujued
 */
@Slf4j
public class DefaultTCRegisterListener implements TCRegisterListener {

    @Override
    public void onRegister(String modId) {
        log.info("register a TC {}", modId);
    }

    @Override
    public void onCancel(String modId) {
        log.info("TC {} was offline", modId);
    }
}
