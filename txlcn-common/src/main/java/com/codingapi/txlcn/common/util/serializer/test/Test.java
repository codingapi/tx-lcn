package com.codingapi.txlcn.common.util.serializer.test;

import com.google.common.collect.ImmutableList;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.RuntimeEnv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws Exception{
        if (RuntimeEnv.ID_STRATEGY instanceof DefaultIdStrategy) {
            ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY).registerDelegate(new ImmutableListDelegate());
        }

        Foo foo = new Foo(ImmutableList.of("foo"));
        Foo foo2 = serializeThenDeserialize(foo);
        System.out.println(foo2);
    }

    private static Foo serializeThenDeserialize(Foo fooToSerialize) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtostuffIOUtil.writeDelimitedTo(out, fooToSerialize, Foo.SCHEMA, buffer());
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        Foo fooDeserialized = Foo.SCHEMA.newMessage();
        ProtostuffIOUtil.mergeDelimitedFrom(in, fooDeserialized, Foo.SCHEMA, buffer());
        return fooDeserialized;
    }

    private static LinkedBuffer buffer() {
        return LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    }
}
