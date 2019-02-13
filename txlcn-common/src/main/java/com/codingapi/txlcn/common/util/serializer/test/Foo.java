package com.codingapi.txlcn.common.util.serializer.test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;


/**
 * This is the POJO I want to serialize. Note that the {@code strings} field refers to an {@link ImmutableList}.
 */

public class Foo {

    public static final Schema<Foo> SCHEMA = RuntimeSchema.getSchema(Foo.class);

    private final ImmutableList<String> strings;

    public Foo(ImmutableList<String> strings) {
        this.strings = Preconditions.checkNotNull(strings);
    }

    public ImmutableList<String> getStrings() {
        return strings;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Foo) {
            Foo that = (Foo) obj;
            return this.strings.equals(that.strings);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return strings.hashCode();
    }

}