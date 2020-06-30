package com.codingapi.txlcn.tc.annotation;

import com.codingapi.maven.uml.annotation.Model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Model(flag = "C",value = "LCN注解",color = "#FF88EE")
public @interface LcnTransaction {


}
