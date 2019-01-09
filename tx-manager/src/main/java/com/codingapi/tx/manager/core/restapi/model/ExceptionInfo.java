package com.codingapi.tx.manager.core.restapi.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionInfo {

    private long id;

    /**
     * 事务组ID
     */
    private String groupId;

    /**
     * 事务单元ID
     */
    private String unitId;

    /**
     * 资源管理服务地址
     */
    private String modId;

    /**
     * 异常情况。-1 【未知】 0 【TxManager通知事务】， 1 【TxClient查询事务状态】 2 【事务发起方通知事务组】
     */
    private int registrar;

    /**
     * 异常状态 0 待处理 1已处理
     */
    private short exState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 事务信息
     */
    private JSONObject transactionInfo;
}
