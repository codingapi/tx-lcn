package com.codingapi.example.common.db.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Demo {
    private Long id;
    private String demoField;
    private String groupId;
    private String unitId;
    private Date createTime;
    private String appName;

}
