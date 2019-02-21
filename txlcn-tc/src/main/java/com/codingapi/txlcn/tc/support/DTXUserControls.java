package com.codingapi.txlcn.tc.support;

import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tracing.TracingContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 19-2-21 上午10:57
 *
 * @author ujued
 */
@Component
public class DTXUserControls implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static TCGlobalContext globalContext;

    public static void rollbackGroup(String groupId) {
        if (Objects.isNull(globalContext)) {
            globalContext = applicationContext.getBean(TCGlobalContext.class);
        }
        globalContext.setRollbackOnly(groupId);
    }

    public static void rollbackCurrentGroup() {
        rollbackGroup(TracingContext.tracing().groupId());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DTXUserControls.applicationContext = applicationContext;
    }
}
