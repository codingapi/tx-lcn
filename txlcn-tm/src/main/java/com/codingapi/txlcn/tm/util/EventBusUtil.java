package com.codingapi.txlcn.tm.util;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/5/9 15:01
 */
@SuppressWarnings("UnstableApiUsage")
public class EventBusUtil {

    private static EventBus eventBus;
    private static AsyncEventBus asyncEventBus;

    private static Executor executor = command -> new Thread(command).start();

    /**
     * 双重锁单例模式
     */
    private static AsyncEventBus getAsyncEventBus() {
        if (asyncEventBus == null) {
            synchronized (AsyncEventBus.class) {
                if (asyncEventBus == null) {
                    asyncEventBus = new AsyncEventBus(executor);
                }
            }
        }
        return asyncEventBus;
    }

    /**
     * 双重锁单例模式
     */
    private static EventBus getEventBus() {
        if (eventBus == null) {
            synchronized (EventBus.class) {
                if (eventBus == null) {
                    eventBus = new EventBus();
                }
            }
        }
        return eventBus;
    }

    public static void post(Object event) {
        getEventBus().post(event);
    }

    /**
     * 异步方式发送事件
     */
    public static void asyncPost(Object event) {
        getAsyncEventBus().post(event);
    }

    public static void register(Object object) {
        getEventBus().register(object);
        getAsyncEventBus().register(object);
    }

}