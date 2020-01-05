package com.codingapi.txlcn.protocol;

import java.util.Optional;

public interface IPeer {

     default <T> T getTarget(Class<T> clazz){
        if(this.getClass().equals(clazz)){
            return (T)this;
        }
        return null;
    }

    default <T> Optional<T> optional(Class<T> clazz){
       Optional<T> optional = Optional.ofNullable(getTarget(clazz));
       return optional;
    }

}
