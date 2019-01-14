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
package com.codingapi.txlcn.spi.message.dto;

import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author lorne
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MessageDto implements Serializable {

    /**
     * 请求动作
     */
    private String action;

    /**
     * 事务组
     */
    private String groupId;

    /**
     * 请求参数
     */
    private byte[] bytes;

    /**
     * 请求状态
     */
    private int state = MessageConstants.STATE_REQUEST;

    public <T> T loadData(Class<T> tClass) throws SerializerException {
        return SerializerContext.getInstance().deSerialize(bytes,tClass);
    }

}
