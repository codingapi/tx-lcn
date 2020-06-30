package com.codingapi.txlcn.tc.utils;

import com.codingapi.maven.uml.annotation.Model;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "C",value = "切面工具类",color = "#FF88EE")
public class PointUtils {

    public static Method targetMethod(ProceedingJoinPoint point) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> targetClass = point.getTarget().getClass();
        return targetClass.getMethod(method.getName(), method.getParameterTypes());
    }
}
