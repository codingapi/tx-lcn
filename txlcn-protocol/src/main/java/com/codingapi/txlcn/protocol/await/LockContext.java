package com.codingapi.txlcn.protocol.await;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Slf4j
public class LockContext {

    private int cacheSize = 1024;

    private static LockContext context = null;

    private Map<String, Lock> map;

    private List<Lock> cacheList;

    private final LinkedList<Lock> freeList;

    public static LockContext getInstance() {
        if (context == null) {
            synchronized (LockContext.class) {
                if (context == null) {
                    context = new LockContext();
                }
            }
        }
        return context;
    }

    private LockContext() {
        map = new ConcurrentHashMap<>();

        cacheList = new CopyOnWriteArrayList<>();

        freeList = new LinkedList<>();
    }


    /**
     * 并发可能会出现脏读
     *
     * @param key key
     * @return hasKey
     */
    public synchronized boolean hasKey(String key) {
        return map.containsKey(key);
    }

    /**
     * 并发可能会出重复添加
     *
     * @param key key
     * @return RpcContent
     */
    public synchronized Lock addKey(String key) {
        Lock lock = createRpcContent();
        map.put(key, lock);
        return lock;
    }


    /**
     * 空闲队列处理
     *
     * @return RpcContent
     */
    private Lock findRpcContent() {
        synchronized (freeList) {
            Lock cacheContent = freeList.getFirst();
            if (!cacheContent.isUsed()) {
                cacheContent.init();
                freeList.remove(cacheContent);
                return cacheContent;
            }
        }

        Lock lock = new Lock();
        lock.init();
        return lock;
    }

    private Lock createRpcContent() {
        if (cacheList.size() < cacheSize) {
            Lock lock = new Lock();
            lock.init();
            cacheList.add(lock);
            return lock;
        } else {
            return findRpcContent();
        }
    }

    public Lock getKey(String key) {
        Lock lock = map.get(key);
        clearKey(key);
        return lock;
    }

    private void clearKey(String key) {
        Lock lock = map.get(key);
        if (cacheList.contains(lock)) {
            synchronized (freeList) {
                freeList.add(lock);
            }
        }
        map.remove(key);
    }


}
