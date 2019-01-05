package com.codingapi.tx.spi.message.params;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Data
public class InitClientParams implements Serializable {

    private String appName;

    private long dtxTime;



}
