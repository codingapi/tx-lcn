package com.codingapi.txlcn.spi.message.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 19-1-22 上午10:53
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DTXLockParams {
    private String contextId;
    private String lockId;
}
