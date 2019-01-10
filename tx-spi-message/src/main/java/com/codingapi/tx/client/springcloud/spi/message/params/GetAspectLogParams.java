package com.codingapi.tx.client.springcloud.spi.message.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/29
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAspectLogParams implements Serializable {
    private String groupId;
    private String unitId;
}
