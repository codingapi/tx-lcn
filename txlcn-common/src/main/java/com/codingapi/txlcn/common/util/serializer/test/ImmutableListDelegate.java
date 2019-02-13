package com.codingapi.txlcn.common.util.serializer.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.protostuff.*;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.util.List;

public class ImmutableListDelegate implements Delegate<ImmutableList<?>> {

    private static final Schema<List> LIST_SCHEMA = RuntimeSchema.getSchema(List.class);

    @Override
    public WireFormat.FieldType getFieldType() {
        return WireFormat.FieldType.MESSAGE;
    }

    @Override
    public ImmutableList<?> readFrom(Input input) throws IOException {
        List<?> list = LIST_SCHEMA.newMessage();
        input.mergeObject(list, LIST_SCHEMA);
        return ImmutableList.copyOf(list);
    }

    @Override
    public void writeTo(Output output, int number, ImmutableList<?> value, boolean repeated) throws IOException {
        List<?> list = Lists.newArrayList(value.asList());
        output.writeObject(number, list, LIST_SCHEMA, repeated);
        LIST_SCHEMA.writeTo(output, list);
    }

    @Override
    public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Class<?> typeClass() {
        return ImmutableList.class;
    }
}
