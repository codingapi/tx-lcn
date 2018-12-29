package com.codingapi.tx.commons.rpc.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxExceptionParams implements Serializable {

    public static final short NOTIFY_UNIT_ERROR = 0;

    public static final short ASK_ERROR = 1;

    public static final short NOTIFY_GROUP_ERROR = 2;

    private String groupId;
    private String unitId;
    private Short registrar;
    private short transactionState;
}
