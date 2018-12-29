package com.codingapi.tx.client.transaction.lcn.control;

import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.framework.control.LCNTransactionControl;
import com.codingapi.tx.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/3
 *
 * @author ujued
 */
@Component("control_lcn_default")
@Slf4j
public class LCNDefaultTransaction implements LCNTransactionControl {
    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // LCN 需要代理资源
        TxTransactionLocal.makeProxy();
    }
}
