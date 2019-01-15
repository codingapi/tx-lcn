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
package com.codingapi.txlcn.client.core.txc.resource.def.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 撤销日志数据对象
 * Date: 2018/12/13
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class UndoLogDO {
    private Long id;
    private String groupId;
    private String unitId;
    private byte[] rollbackInfo;
    private long gmtCreate = System.currentTimeMillis();
    private long gmtModified = System.currentTimeMillis();
}
