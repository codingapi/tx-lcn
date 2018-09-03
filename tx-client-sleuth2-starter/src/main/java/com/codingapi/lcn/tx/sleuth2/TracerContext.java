package com.codingapi.lcn.tx.sleuth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lorne
 * @date 2018/9/3
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TracerContext {

    private String spanId;

    private String tracerId;

}
