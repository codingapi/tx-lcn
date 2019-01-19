package com.codingapi.txlcn.logger.model;

import com.codingapi.txlcn.logger.db.TxLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogList {
    private long total;
    private List<TxLog> txLogs;
}
