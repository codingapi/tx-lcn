package com.codingapi.tm.compensate.service.impl;

import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.compensate.service.CompensateService;
import com.codingapi.tm.compensate.dao.CompensateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    @Autowired
    private CompensateDao compensateDao;

    @Override
    public boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg) {

        return compensateDao.saveCompensateMsg(transactionCompensateMsg);
    }
}
