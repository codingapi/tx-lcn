package com.codingapi.txlcn.client.core.lcn.control;

import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.support.TXLCNTransactionControl;
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
