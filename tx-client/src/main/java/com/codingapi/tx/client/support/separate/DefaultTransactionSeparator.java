package com.codingapi.tx.client.support.separate;

import org.springframework.stereotype.Component;

/**
 * Description: 默认的事务分离器
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Component("transaction_state_resolver_default")
public class DefaultTransactionSeparator extends CustomizableTransactionSeparator {
}
