package com.codingapi.txlcn.spi.message.params;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-24 下午5:42
 *
 * @author ujued
 */
@Data
public class RelayNotifyUnitParams implements Serializable {
    private NotifyUnitParams notifyUnitParams;
    private String modId;
}
