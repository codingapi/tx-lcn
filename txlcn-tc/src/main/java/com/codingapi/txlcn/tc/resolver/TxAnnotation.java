package com.codingapi.txlcn.tc.resolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事务注解信息
 * @author lorne 2020-03-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxAnnotation {

    /**
     * 注解对象 例如LCNAnnotation TCCAnnotation MQAnnotation等
     */
    private Object annotation;
    /**
     * 事务类型
     * LCN TCC MQ
     */
    private String type;


}
