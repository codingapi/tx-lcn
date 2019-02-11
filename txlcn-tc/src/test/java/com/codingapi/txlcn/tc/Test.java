package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.tc.core.transaction.txc.analy.util.SqlUtils;

public class Test {
    public static void main(String[] args) {

        TestBean testBean = new TestBean();
        System.out.println(testBean.getTime());
        System.out.println(testBean.getTimestamp());
        byte[] bytes =  SqlUtils.objectToBlob(testBean);
        TestBean testBean2 =  SqlUtils.blobToObject(bytes,TestBean.class);
        System.out.println("-----");
        System.out.println(testBean2.getTime());
        System.out.println(testBean2.getTimestamp());
    }
}
