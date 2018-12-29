package com.codingapi.tx.spi.sleuth.listener;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/13
 *
 * @author ujued
 */
public interface SleuthParamListener {


    List<String> beforeBalance(String localKey);


    void alfterNewBalance(String key);

}
