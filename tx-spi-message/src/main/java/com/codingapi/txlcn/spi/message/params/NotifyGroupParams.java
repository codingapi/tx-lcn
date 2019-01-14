package com.codingapi.txlcn.spi.message.params;

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
public class NotifyGroupParams implements Serializable {
    private String groupId;
    private int state;
}
