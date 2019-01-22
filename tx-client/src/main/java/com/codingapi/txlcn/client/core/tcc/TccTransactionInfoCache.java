package com.codingapi.txlcn.client.core.tcc;

import com.codingapi.txlcn.client.bean.TccTransactionInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Date: 19-1-16 下午1:42
 *
 * @author ujued
 */
@Component
public class TccTransactionInfoCache extends ConcurrentHashMap<String, TccTransactionInfo> {

}
