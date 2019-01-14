package com.codingapi.txlcn.manager.core.group;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/4
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransUnit {

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
    public String toString() {
        return JSON.toJSONString(this);
    }
}
