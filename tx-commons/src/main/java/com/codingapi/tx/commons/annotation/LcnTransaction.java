package com.codingapi.tx.commons.annotation;

<<<<<<< HEAD
import com.codingapi.tx.commons.util.DTXFunctions;
=======
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 4a50e1937323b1e4422f87687c88cc6401007275

import java.lang.annotation.*;

/**
 * Description: type [lcn] of DTX
 * Date: 1/4/19
 *
 * @author ujued
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
public @interface LcnTransaction {

    /**
     * 事务单元职责
     *
     * @return 功能代号
     * @see DTXFunctions
     */
    int func() default DTXFunctions.CREATE_OR_JOIN;
}
