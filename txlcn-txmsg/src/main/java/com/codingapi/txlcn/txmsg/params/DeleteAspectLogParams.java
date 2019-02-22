package com.codingapi.txlcn.txmsg.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-2-12 上午10:47
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteAspectLogParams implements Serializable {
    private String groupId;
    private String unitId;
}
