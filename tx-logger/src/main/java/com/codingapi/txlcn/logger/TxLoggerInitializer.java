package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.logger.helper.TxlcnLogDbHelper;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/16
 *
 * @author codingapi
 */
public class TxLoggerInitializer implements TxLcnInitializer {
    
    private TxlcnLogDbHelper mysqlLoggerHelper;
    
    public TxLoggerInitializer(TxlcnLogDbHelper mysqlLoggerHelper) {
        this.mysqlLoggerHelper = mysqlLoggerHelper;
    }
    
    
    @Override
    public void init() throws Exception {
        mysqlLoggerHelper.init();
    }
}
