package com.codingapi.txlcn.tm.cluster;

import lombok.Data;

/**
 * Description:
 * Date: 19-1-25 上午10:31
 *
 * @author ujued
 */
@Data
public class TMProperties {
    private String host;
    private Integer httpPort;
    private Integer transactionPort;
}
