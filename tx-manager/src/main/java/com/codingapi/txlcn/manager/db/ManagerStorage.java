package com.codingapi.txlcn.manager.db;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
public interface ManagerStorage {


    /**
     * 获取Manager地址列表
     * @return  addressList
     */
    List<String> addressList();

    void remove(String address);
}
