package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.logger.helper.TxLcnLogDbHelper;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
public class TxLoggerInitializer implements TxLcnInitializer {
    
    private TxLcnLogDbHelper mysqlLoggerHelper;
    
    public TxLoggerInitializer(TxLcnLogDbHelper mysqlLoggerHelper) {
        this.mysqlLoggerHelper = mysqlLoggerHelper;
    }
    
    
    @Override
    public void init() throws Exception {
        mysqlLoggerHelper.init();
    }
}
