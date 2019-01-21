package com.codingapi.txlcn.client.core.txc.resource.undo;

import com.codingapi.txlcn.client.core.txc.resource.def.bean.FieldCluster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 19-1-21 上午10:19
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TableRecord {
    private String tableName;
    private FieldCluster fieldCluster;
}
