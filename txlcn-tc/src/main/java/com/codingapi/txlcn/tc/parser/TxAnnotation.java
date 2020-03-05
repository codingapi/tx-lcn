package com.codingapi.txlcn.tc.parser;

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
public class TxAnnotation {

    private Object annotation;
    private String type;


}
