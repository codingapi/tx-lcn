package com.codingapi.lcn.tx.annotation;


import java.lang.annotation.*;

/**
 *
 * @author lorne
 * @date 2018/8/30
 * @description LCN分布式事务注解
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TxTransaction {


    /**
     * 标示本服务是否是只读
     * 若为true : 不会加入事务组; Connection 不会被 Wrap; 事务信息能正常传递
     * 在本服务无DB操作或仅有查询时请配置 true 将提高性能
     * 若应用都没有DB配置，此配置无意义不用设值
     */
    boolean readOnly() default false;


    TxTransactionMode mode() default TxTransactionMode.TX_MODE_LCN;


}
