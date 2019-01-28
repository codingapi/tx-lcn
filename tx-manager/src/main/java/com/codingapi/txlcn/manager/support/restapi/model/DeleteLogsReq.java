package com.codingapi.txlcn.manager.support.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 19-1-17 下午6:39
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteLogsReq {
    private String groupId;
    private String tag;
    private String lTime;
    private String rTime;
}