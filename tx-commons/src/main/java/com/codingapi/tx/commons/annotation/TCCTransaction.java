package com.codingapi.tx.commons.annotation;

import java.lang.annotation.*;

/**
 * @author 侯存路
 * @date 2018/12/3
 * @company codingApi
 * @description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TCCTransaction {



    /**
     *
     *  tcc事务回调执行类  该类需交由spring管理
     * @return
     */
    Class<?> executeClass();


    /**
     *  确认事务执行方法
     *  该方法参数需要和事务单元的参数保持一致
     * @return
     */
    String confirmMethod();


    /**
     *  取消事务执行方法
     *  该方法参数需要和事务单元的参数保持一致
     * @return
     */
    String cancelMethod();



}
