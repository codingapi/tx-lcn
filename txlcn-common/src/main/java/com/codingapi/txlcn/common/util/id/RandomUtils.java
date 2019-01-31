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
package com.codingapi.txlcn.common.util.id;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lorne 2018/12/2
 */
public class RandomUtils {

    private static volatile IdGen theIdGen;

    public static void init(IdGen idGen) {
        theIdGen = idGen;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String randomKey() {
        if (Objects.isNull(theIdGen)) {
            theIdGen = System::nanoTime;
        }
        return String.valueOf(theIdGen.nextId());
    }

    public static String simpleKey() {
        return String.valueOf(System.nanoTime());
    }

    static volatile String last = "";

    public static void main(String[] args) {
        IdGenInit.applySnowFlakeIdGen(10, 1);
        System.out.println(randomKey());

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                String now = randomKey();
                if (now.equals(last)) {
                    System.out.println("error");
                }
                last = now;
            });
        }
    }
}
