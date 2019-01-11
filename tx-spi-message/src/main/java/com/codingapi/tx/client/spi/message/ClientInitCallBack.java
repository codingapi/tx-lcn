package com.codingapi.tx.client.spi.message;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
public interface ClientInitCallBack {


    /**
     * 初始化连接成功回调
     * @param remoteKey 远程调用唯一key
     */
    void connected(String remoteKey);


}
