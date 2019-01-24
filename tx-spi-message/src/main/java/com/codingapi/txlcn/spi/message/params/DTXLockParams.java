package com.codingapi.txlcn.spi.message.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Set;

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
    private String groupId;
    private String contextId;
    private Set<String> locks;
    private int lockType;
}
