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

import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.util.StringUtils;

/**
 * Description: 业务Select语句信息。附加分析此查询是否要锁定
 * Date: 2018/12/17
 *
 * @author ujued
 */
public class LockableSelect {

    private Select select;

    public LockableSelect(Select select) {
        this.select = select;
    }

    public Select statement() {
        return select;
    }

    public boolean isxLock() {
        return StringUtils.endsWithIgnoreCase(StringUtils.trimAllWhitespace(this.select.toString()),
                StringUtils.trimAllWhitespace(SqlUtils.FOR_UPDATE));
    }

    public boolean issLock() {
        return StringUtils.endsWithIgnoreCase(StringUtils.trimAllWhitespace(this.select.toString()),
                StringUtils.trimAllWhitespace(SqlUtils.LOCK_IN_SHARE_MODE));
    }

    public boolean shouldLock() {
        return isxLock() || issLock();
    }
}
