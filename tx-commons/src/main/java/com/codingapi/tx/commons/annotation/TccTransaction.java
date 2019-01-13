package com.codingapi.tx.commons.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * @author 侯存路
 * @date 2018/12/3
 * @company codingApi
 * @description type [tcc] of DTX
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
public @interface TccTransaction {

    /**
     * tcc事务回调执行类  该类需交由spring管理
     *
     * @return
     */
    Class<?> executeClass() default Void.class;


    /**
     * 确认事务执行方法
     * 该方法参数需要和事务单元的参数保持一致
     *
     * @return
     */
    String confirmMethod() default "";


    /**
     * 取消事务执行方法
     * 该方法参数需要和事务单元的参数保持一致
     *
     * @return
     */
    String cancelMethod() default "";

    /**
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see DTXPropagation
     */
    DTXPropagation dtxp() default DTXPropagation.REQUIRED;
}
