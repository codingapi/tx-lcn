package com.codingapi.tx.client.core.txc.control;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.core.txc.resource.def.bean.RollbackInfo;
import com.codingapi.tx.client.support.TXLCNTransactionControl;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_default")
public class TxcDefaultTransaction implements TXLCNTransactionControl {

    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // 准备回滚信息容器
        DTXLocal.cur().setAttachment(new RollbackInfo());
        // TXC 类型事务需要代理资源
        DTXLocal.makeProxy();
    }
}
