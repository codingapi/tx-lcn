package com.codingapi.txlcn.tc.core.transaction.txc.analy;

import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.PrimaryKeysProvider;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.util.AnnotationUtils;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.util.ClassUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author dinghuang123@gmail.com
 * @since 2019/7/28
 */
@Component
public class PrimaryKeyListVisitorHandler implements PrimaryKeysProvider {

    @Value("${tx-lcn.primary-key-package}")
    private String primaryKeyPackage;

    @Override
    public Map<String, List<String>> provide() {
        if (primaryKeyPackage != null) {
            //扫描所有注解，把主键拿进来
            // 获取特定包下所有的类(包括接口和类)
            List<Class<?>> clsList = ClassUtils.getClasses(primaryKeyPackage);
            //输出所有使用了特定注解的类的注解值
            return AnnotationUtils.getPrimaryKeyList(clsList);
        } else {
            return null;
        }
    }

}
