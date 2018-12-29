package com.codingapi.tx.manager.restapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DTXInfo {

    /**
     * 本周处理事务数量
     */
    private long dtxCount;

    /**
     * 本周失败的事务数量
     */
    private int errorDtxCount;

    /**
     * 今天事务数量
     */
    private int todayDtxCount;

    /**
     * 今天失败的事务数量
     */
    private int todayErrorDtxCount;

}
