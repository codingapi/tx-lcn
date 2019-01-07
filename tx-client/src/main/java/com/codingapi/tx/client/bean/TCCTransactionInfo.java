package com.codingapi.tx.client.bean;

import lombok.Data;

/**
 * @author 侯存路
 * @date 2018/12/3
 * @company codingApi
 * @description
 */
@Data
public class TCCTransactionInfo {


    /**
     * Tcc 事务 提交/回滚 执行类
     */
    private  Class<?> executeClass;


    /**
     *  回滚方法名称
     */
    private  String cancelMethod;


    /**
     *  提交名称
     */
    private  String confirmMethod;


    /**
     *  参数
     */
    private  Object [] methodParameter;


    /**
     *  参数类型
     */
    private  Class [] methodTypeParameter;



}
