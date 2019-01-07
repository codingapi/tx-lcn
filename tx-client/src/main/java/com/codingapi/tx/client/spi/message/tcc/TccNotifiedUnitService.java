package com.codingapi.tx.client.spi.message.tcc;

import com.codingapi.tx.client.support.common.DefaultNotifiedUnitService;
import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author 侯存路
 */
@Service("rpc_tcc_notify-unit")
@Slf4j
public class TccNotifiedUnitService extends DefaultNotifiedUnitService {

    @Autowired
    public TccNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate) {
        super(transactionCleanTemplate);
    }
}
