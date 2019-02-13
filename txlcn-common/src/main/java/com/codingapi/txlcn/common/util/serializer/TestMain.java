package com.codingapi.txlcn.common.util.serializer;

import com.codingapi.txlcn.common.exception.SerializerException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class TestMain {

    public static void main(String[] args) throws SerializerException {

        TestBean testBean = new TestBean();
        Multimap<String,String> listMultimap = LinkedHashMultimap.create();
        listMultimap.put("name","hello");
        testBean.setMultimap(listMultimap);
        System.out.println(testBean);
        System.out.println(testBean.getMultimap());
        byte[] bytes =  SerializerContext.getInstance().serialize(testBean);
        TestBean testBean2 =  SerializerContext.getInstance().deSerialize(bytes,TestBean.class);
        System.out.println(testBean2);
        System.out.println(testBean2.getMultimap());

        //https://stackoverflow.com/questions/12352559/how-do-you-serialize-guavas-immutable-collections-using-protostuff
    }
}
