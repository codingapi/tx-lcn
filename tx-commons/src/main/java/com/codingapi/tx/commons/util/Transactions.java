package com.codingapi.tx.commons.util;

import org.springframework.util.DigestUtils;

/**
 * Description: 事务相关工具类
 * Date: 2018/12/17
 *
 * @author ujued
 */
public class Transactions {

    /////////// 事务类型  //////////////////

    public static final String LCN = "lcn";

    public static final String TCC = "tcc";

    public static final String TXC = "txc";

    /////////// 常量 //////////////////////

    public static final String TAG_TRANSACTION = "transaction";

    public static final String TAG_TASK = "task";

    public static final String TAG_COMPENSATION = "compensation";


    /////////// 工具方法  ////////////////////////////////////////////

    /**
     * 方法签名生成事务单元ID
     *
     * @param methodSignature
     * @return
     */
    public static String unitId(String methodSignature) {
        return DigestUtils.md5DigestAsHex(methodSignature.getBytes());
    }

    /**
     * 方法签名生成补偿ID
     *
     * @param startMethodSignature
     * @return
     */
    public static String compensationId(String startMethodSignature) {
        return DigestUtils.md5DigestAsHex(startMethodSignature.getBytes());
    }
}
