package com.codingapi.tx.manager.restapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxManagerLog {
    private Long id;
    /**
     * 事务组ID
     */
    private String groupId;

    /**
     * 事务单元ID
     */
    private String unitId;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createTime;
}
