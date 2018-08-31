package com.codingapi.lcn.tx.demo.a.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */

@Data
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class User {


    private Long id;


    private String name;


}
