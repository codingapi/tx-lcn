package com.codingapi.tx.commons.rpc.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotifyConnectParams implements Serializable {

    private String host;
    private int port;

}
