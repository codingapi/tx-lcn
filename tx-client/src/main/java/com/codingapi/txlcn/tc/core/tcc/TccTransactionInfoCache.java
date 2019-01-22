package com.codingapi.txlcn.tc.core.tcc;

import com.codingapi.txlcn.tc.bean.TccTransactionInfo;
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
