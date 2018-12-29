package com.codingapi.tx.manager.restapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxLogList {

    private long total;

    private List<TxManagerLog> logs;
}
