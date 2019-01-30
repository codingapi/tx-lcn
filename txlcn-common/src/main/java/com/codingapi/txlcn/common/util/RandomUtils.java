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
package com.codingapi.txlcn.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author lorne 2018/12/2
 */
public class RandomUtils {
    
    private static Random random = new Random();

    private static IdWorker idWorker = new IdWorker(random.nextInt(1024));

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-" , "");
    }
    
    public static String randomKey() {
        try {
            return String.valueOf(idWorker.nextId());
        } catch (Exception e) {
            return getUUID();
        }
    }

}
