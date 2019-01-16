package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.logger.db.TxLcnLoggerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
@Component
public class TxLoggerInitializer implements TxLcnInitializer {

    @Autowired
    private TxLcnLoggerHelper txLcnLoggerHelper;

    @Override
    public void init() throws Exception {
        txLcnLoggerHelper.init();
    }
}
