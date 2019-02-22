/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tm.support.db.domain;

import com.codingapi.txlcn.txmsg.params.TxExceptionParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "t_tx_exception")
public class TxException {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 事务组ID
     */
    @Column(length = 60)
    private String groupId;

    /**
     * 事务单元ID
     */
    @Column(length = 60)
    private String unitId;

    /**
     * 资源管理服务地址
     */
    @Column(length = 100)
    private String modId;

    /**
     * 事务状态
     */
    private Integer transactionState;

    /**
     * 上报方
     * @see TxExceptionParams
     */
    private short registrar;

    /**
     * 异常状态 0 待处理 1已处理
     */
    private short exState;

    /**
     * 发生时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
}
