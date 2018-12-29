package com.codingapi.tx.manager.restapi.vo;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @auther ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private int code;
    private String message;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
