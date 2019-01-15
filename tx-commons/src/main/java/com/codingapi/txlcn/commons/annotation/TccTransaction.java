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
 * @author 侯存路 2018/12/3
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TccTransaction {

    /**
     * tcc事务回调执行类  该类需交由spring管理
     *
     * @return 作用类对象
     */
    Class<?> executeClass() default Void.class;


    /**
     * 确认事务执行方法
     * 该方法参数需要和事务单元的参数保持一致
     *
     * @return  确认方法
     */
    String confirmMethod() default "";


    /**
     * 取消事务执行方法
     * 该方法参数需要和事务单元的参数保持一致
     *
     * @return  取消方法
     */
    String cancelMethod() default "";

    /**
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see DTXPropagation
     */
    DTXPropagation dtxp() default DTXPropagation.REQUIRED;
}
