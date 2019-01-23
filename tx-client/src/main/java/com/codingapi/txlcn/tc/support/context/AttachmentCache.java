package com.codingapi.txlcn.tc.support.context;

/**
 * Description:
 * Date: 19-1-23 下午12:03
 *
 * @author ujued
 */
public interface AttachmentCache {

    void attach(String groupId, String key, Object attachment);

    <T> T attachment(String groupId, String key);

    void remove(String groupId, String key);

    boolean containsKey(String groupId, String key);

    void remove(String groupId);
}
