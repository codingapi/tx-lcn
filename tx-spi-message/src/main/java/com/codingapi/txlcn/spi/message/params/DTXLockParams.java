package com.codingapi.txlcn.spi.message.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-22 上午10:53
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DTXLockParams implements Serializable {
    private String contextId;
    private String lockId;
    private int lockType;
}
