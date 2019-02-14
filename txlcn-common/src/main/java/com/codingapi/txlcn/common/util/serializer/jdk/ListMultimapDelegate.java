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
package com.codingapi.txlcn.common.util.serializer.jdk;

import com.google.common.collect.ListMultimap;
import io.protostuff.*;
import io.protostuff.runtime.Delegate;

import java.io.IOException;

/**
 * JavaSerializer. ListMultimapDelegate
 *
 * @author lorne
 */
public class ListMultimapDelegate implements Delegate<ListMultimap> {

    private JavaSerializer javaSerializer = new JavaSerializer();

    @Override
    public WireFormat.FieldType getFieldType() {
        return WireFormat.FieldType.BYTES;
    }

    public ListMultimap readFrom(Input input) throws IOException {
        return javaSerializer.deSerialize(input.readBytes().toByteArray(),typeClass());
    }

    public void writeTo(Output output, int number, ListMultimap value,
                        boolean repeated) throws IOException {
        byte[] bytes =  javaSerializer.serialize(value);
        output.writeBytes(number, ByteString.copyFrom(bytes), repeated);
    }

    public void transfer(Pipe pipe, Input input, Output output, int number,
                         boolean repeated) throws IOException {
        output.writeBytes(number, input.readBytes(), repeated);
    }

    @Override
    public Class<ListMultimap> typeClass() {
        return ListMultimap.class;
    }
}
