package com.codingapi.txlcn.manager.support.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 19-1-18 上午11:48
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteExceptions {
    private List<Long> id;
}