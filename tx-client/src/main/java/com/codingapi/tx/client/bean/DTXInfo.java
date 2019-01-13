package com.codingapi.tx.client.bean;

import com.codingapi.tx.commons.annotation.DTXPropagation;
import com.codingapi.tx.commons.bean.TransactionInfo;
import com.codingapi.tx.commons.util.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * Description:
 * Date: 19-1-11 下午1:21
 *
 * @author ujued
 */
@AllArgsConstructor
@Data
public class DTXInfo {

    private String transactionType;

    private DTXPropagation transactionPropagation;

    private TransactionInfo transactionInfo;

    /**
     * 用户实例对象的业务方法（包含注解信息）
     */
    private Method businessMethod;

    private String unitId;

    public DTXInfo(Method method, Object[] args, Class<?> targetClass) {
        this.transactionInfo = new TransactionInfo();
        this.transactionInfo.setTargetClazz(targetClass);
        this.transactionInfo.setArgumentValues(args);
        this.transactionInfo.setMethod(method.getName());
        this.transactionInfo.setMethodStr(method.toString());
        this.transactionInfo.setParameterTypes(method.getParameterTypes());

        this.businessMethod = method;
        this.unitId = Transactions.unitId(method.toString());
    }

    public void reanalyseMethodArgs(Object[] args) {
        this.transactionInfo.setArgumentValues(args);
    }
}