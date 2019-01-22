package com.codingapi.txlcn.tc.support.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Date: 19-1-16 下午9:23
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTXContext {

    /**
     * 事务组锁
     */
    private Object lock = new Object();

    /**
     * 上下文内分布式事务类型
     */
    private Set<String> transactionTypes = new HashSet<>(6);
}
