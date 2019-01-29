package com.codingapi.txlcn.tc.core.txc.analy.undo;

import com.codingapi.txlcn.tc.core.txc.analy.def.bean.FieldCluster;
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
