package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.logger.db.TxLcnLoggerHelper;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
public class TxLoggerInitializer implements TxLcnInitializer {
    
    private TxLcnLoggerHelper txLcnLoggerHelper;
    
    public TxLoggerInitializer(TxLcnLoggerHelper txLcnLoggerHelper) {
        this.txLcnLoggerHelper = txLcnLoggerHelper;
    }
    
    
    @Override
    public void init() throws Exception {
        txLcnLoggerHelper.init();
    }
}
