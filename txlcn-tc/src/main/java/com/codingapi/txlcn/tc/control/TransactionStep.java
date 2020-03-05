package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.info.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStep {

     private TransactionInfo transactionInfo;

     private Step step;

     public enum Step{
         CREATE,JOIN,NOTIFY
     }
}
