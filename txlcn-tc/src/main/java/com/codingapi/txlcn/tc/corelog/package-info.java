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
/**
 * Description:
 * Date: 19-1-22 上午9:27
 *
 * @author ujued
 */
package com.codingapi.txlcn.tc.corelog;

// TC's core log used h2 database. don't care the special SQL statement.

// What's the core log?
// 1. aspect log：Transaction Unit 's execute context.
// 2. TXC undo log: reverse sql info of TXC transaction type.