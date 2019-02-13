package com.codingapi.txlcn.common.util.serializer;

import com.google.common.collect.Multimap;

import java.io.Serializable;

public class TestBean implements Serializable{

    private Multimap multimap;

    public Multimap getMultimap() {
        return multimap;
    }

    public void setMultimap(Multimap multimap) {
        this.multimap = multimap;
    }
}
