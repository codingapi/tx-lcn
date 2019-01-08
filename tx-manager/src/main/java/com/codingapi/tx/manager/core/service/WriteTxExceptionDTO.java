package com.codingapi.tx.manager.core.service;

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
public class WriteTxExceptionDTO {
    private String groupId;
    private String unitId;
    private String modId;
    private Short transactionState;
    private Short registrar;

    public WriteTxExceptionDTO(String groupId, String unitId, String modId, Short transactionState) {
        this.groupId = groupId;
        this.unitId = unitId;
        this.transactionState = transactionState;
        this.modId = modId;
    }
}
