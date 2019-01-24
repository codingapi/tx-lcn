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
package com.codingapi.txlcn.tc.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 */
public class DependenciesImportSelector implements ImportSelector {

    /**
     * resolve the spi classes
     *
     * @param importingClassMetadata importingClassMetadata
     * @return spi classes
     */
    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "com.codingapi.txlcn.spi.MessageConfiguration",
                "com.codingapi.txlcn.tc.spi.SleuthConfiguration"
        };
    }
}
