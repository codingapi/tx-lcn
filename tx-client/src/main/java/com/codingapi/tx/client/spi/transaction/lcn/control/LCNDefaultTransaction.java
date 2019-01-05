package com.codingapi.tx.client.spi.transaction.lcn.control;

import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.support.separate.TXLCNTransactionControl;
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
public class LCNDefaultTransaction implements TXLCNTransactionControl {
    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // LCN 需要代理资源
        DTXLocal.makeProxy();
    }
}
