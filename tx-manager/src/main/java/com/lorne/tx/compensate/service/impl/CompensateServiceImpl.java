package com.lorne.tx.compensate.service.impl;

import com.lorne.tx.compensate.dao.CompensateDao;
import com.lorne.tx.compensate.model.TransactionCompensateMsg;
import com.lorne.tx.compensate.service.CompensateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService{


    @Autowired
    private CompensateDao compensateDao;

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {

        return compensateDao.saveCompensateMsg(transactionCompensateMsg);
    }
}
