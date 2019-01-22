package com.codingapi.txlcn.tc.support.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 19-1-16 下午9:23
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTXGroupContext {
    private Object lock = new Object();
}
