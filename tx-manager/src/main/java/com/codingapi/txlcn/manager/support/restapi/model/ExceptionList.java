package com.codingapi.txlcn.manager.support.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionList {
    private long total;
    private List<ExceptionInfo> exceptions;
}
