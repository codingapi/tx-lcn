package com.codingapi.txlcn.common.util.serializer;



import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.WireFormat;
import io.protostuff.runtime.Delegate;

import java.io.IOException;
import java.sql.Date;

public class DateDelegate implements Delegate<Date> {

    @Override
    public WireFormat.FieldType getFieldType() {
        return WireFormat.FieldType.FIXED64;
    }

    public Date readFrom(Input input) throws IOException {
        return new Date(input.readFixed64());
    }

    public void writeTo(Output output, int number, Date value,
                        boolean repeated) throws IOException {
        output.writeFixed64(number, value.getTime(), repeated);
    }

    public void transfer(Pipe pipe, Input input, Output output, int number,
                         boolean repeated) throws IOException {
        output.writeFixed64(number, input.readFixed64(), repeated);
    }

    @Override
    public Class<?> typeClass() {
        return Date.class;
    }
}
