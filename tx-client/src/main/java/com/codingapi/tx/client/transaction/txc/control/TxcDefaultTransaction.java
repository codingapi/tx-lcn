package com.codingapi.tx.client.transaction.txc.control;

import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.framework.control.LCNTransactionControl;
import com.codingapi.tx.commons.exception.TxClientException;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_default")
public class TxcDefaultTransaction implements LCNTransactionControl {

    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // TXC 类型事务需要代理资源
        TxTransactionLocal.makeProxy();
    }
}
