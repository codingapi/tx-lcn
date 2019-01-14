package com.codingapi.txlcn.client.message.transaction;

import com.codingapi.txlcn.client.support.common.DefaultNotifiedUnitService;
import com.codingapi.txlcn.client.support.common.template.TransactionCleanTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_lcn_notify-unit")
@Slf4j
public class LcnNotifiedUnitService extends DefaultNotifiedUnitService {

    @Autowired
    public LcnNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate) {
        super(transactionCleanTemplate);
    }
}
