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
package com.codingapi.txlcn.commons.annotation;

import java.lang.annotation.*;

/**
 * Description: type [txc] of DTX
 * Date: 1/4/19
 *
 * @author ujued
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TxcTransaction {

    /**
     * 资源锁定时等待时间，默认不等待。（可能会在下一个小版本实现）
     *
     * @return 等待时间
     */
    long timeout() default 0;

    /**
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see DTXPropagation
     */
    DTXPropagation dtxp() default DTXPropagation.REQUIRED;
}
