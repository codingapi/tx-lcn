package com.codingapi.tx.commons.annotation;

import com.codingapi.tx.commons.util.DTXFunctions;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Description: type [txc] of DTX
 * Date: 1/4/19
 *
 * @author ujued
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
public @interface TxcTransaction {

    /**
     * 资源锁定时等待时间，默认不等待。（可能会在下一个版本实现）
     *
     * @return 等待时间
     */
    long timeout() default 0;

    /**
     * 事务单元职责
     *
     * @return 功能代号
     * @see DTXFunctions
     */
    int func() default DTXFunctions.CREATE_OR_JOIN;
}
