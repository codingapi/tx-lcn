package com.codingapi.txlcn.tm.core.storage;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description:
 * Date: 19-1-21 下午3:13
 *
 * @author ujued
 */
@Data
public class TransactionUnit implements Serializable {

    /**
     * 事务单元标识
     */
    private String remoteKey;

    /**
     * 事务类型
     */
    private String unitType;

    /**
     * 相关业务方法签名
     */
    private String unitId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionUnit that = (TransactionUnit) o;
        return Objects.equals(unitId, that.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitId);
    }
}
