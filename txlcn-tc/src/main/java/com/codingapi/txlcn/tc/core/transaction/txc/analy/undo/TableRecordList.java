package com.codingapi.txlcn.tc.core.transaction.txc.analy.undo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Date: 19-1-21 下午1:53
 *
 * @author ujued
 */
@Data
public class TableRecordList implements Serializable {
    private List<TableRecord> tableRecords = new ArrayList<>();
}
