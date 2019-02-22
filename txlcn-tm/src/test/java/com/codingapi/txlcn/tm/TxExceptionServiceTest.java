package com.codingapi.txlcn.tm;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.common.exception.TransactionStateException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.support.restapi.ao.WriteTxExceptionDTO;
import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author meetzy
 * @date 2019-02-14 14:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TMApplication.class)
@ActiveProfiles("meetzy")
public class TxExceptionServiceTest {
    @Autowired
    private TxExceptionService txExceptionService;

    @Test
    public void writeTxException(){
        WriteTxExceptionDTO w = new WriteTxExceptionDTO();
        w.setRegistrar((short) 0);
        w.setGroupId("14141414");
        w.setModId("41414141");
        w.setRemark("Test");
        w.setTransactionState(1);
        w.setUnitId("2019");
        txExceptionService.writeTxException(w);
    }
    
    @Test
    public void transactionState(){
        System.out.println(txExceptionService.transactionState("14141414"));
    }
    
    @Test
    public void exceptionList(){
    
        System.out.println(JSON.toJSONString(txExceptionService.exceptionList(0,1,-2,"",-2)));
    }
    
    @Test
    public void getTransactionInfo() throws TransactionStateException, TxManagerException {
        System.out.println(txExceptionService.getTransactionInfo("14141414","2019").toJSONString());
    }
    
    @Test
    public void deleteExceptions() throws TxManagerException {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        txExceptionService.deleteExceptions(ids);
    }
    
    @Test
    public void deleteTransactionInfo() throws TransactionStateException, TxManagerException {
        System.out.println(txExceptionService.getTransactionInfo("14141414","2019").toJSONString());
    }
}
