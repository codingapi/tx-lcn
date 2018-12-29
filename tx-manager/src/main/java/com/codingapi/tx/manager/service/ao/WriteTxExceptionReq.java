package com.codingapi.tx.manager.service.ao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WriteTxExceptionReq {
    private String groupId;
    private String unitId;
    private String clientAddress;
    private Short transactionState;
    private short exState;
    private Short registrar;

    public WriteTxExceptionReq(String groupId, String unitId, String clientAddress, Short transactionState) {
        this.groupId = groupId;
        this.unitId = unitId;
        this.transactionState = transactionState;
        this.clientAddress = clientAddress;
    }
}
