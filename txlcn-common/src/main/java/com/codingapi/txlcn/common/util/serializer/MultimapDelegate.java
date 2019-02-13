//package com.codingapi.txlcn.common.util.serializer;
//
//import com.dyuproject.protostuff.Input;
//import com.dyuproject.protostuff.Output;
//import com.dyuproject.protostuff.Pipe;
//import com.dyuproject.protostuff.WireFormat;
//import com.dyuproject.protostuff.runtime.Delegate;
//import com.google.common.collect.Multimap;
//
//import java.io.IOException;
//
//public class MultimapDelegate implements Delegate<Multimap> {
//
//    @Override
//    public WireFormat.FieldType getFieldType() {
//        return WireFormat.FieldType.BYTES;
//    }
//
//    public Multimap readFrom(Input input) throws IOException {
//        return new Multimap(input.readFixed64());
//    }
//
//    public void writeTo(Output output, int number, Multimap value,
//                        boolean repeated) throws IOException {
//        output.writeFixed64(number, value.asMap(), repeated);
//    }
//
//    public void transfer(Pipe pipe, Input input, Output output, int number,
//                         boolean repeated) throws IOException {
//        output.writeBytes(number, input.readFixed64(), repeated);
//    }
//
//    @Override
//    public Class<?> typeClass() {
//        return Multimap.class;
//    }
//}
