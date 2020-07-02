package com.codingapi.txlcn.tc.core.transaction.txc.analy.util;

import com.codingapi.txlcn.tc.annotation.TableId;
import com.codingapi.txlcn.tc.annotation.TableName;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dinghuang123@gmail.com
 * @since 2019/7/29
 */
public class AnnotationUtils {

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static Map<String, List<String>> getPrimaryKeyList(List<Class<?>> clsList) {
        if (clsList != null && clsList.size() > 0) {
            Map<String, List<String>> map = new HashMap<>(clsList.size());
            for (Class<?> cls : clsList) {
                //获取类中的所有的方法
                boolean classHasAnnotation = cls.isAnnotationPresent(TableName.class);
                if (classHasAnnotation) {
                    TableName tableName = cls.getAnnotation(TableName.class);
                    if (tableName.value().length() != 0) {
                        Field[] fields = cls.getDeclaredFields();
                        String tableIdVale = null;
                        for (Field field : fields) {
                            boolean fieldHasAnnotation = field.isAnnotationPresent(TableId.class);
                            if (fieldHasAnnotation) {
                                TableId tableId = field.getAnnotation(TableId.class);
                                //输出注解属性
                                if (tableId.value().length() != 0) {
                                    tableIdVale = tableId.value();
                                } else {
                                    //转驼峰
                                    tableIdVale = humpToLine(field.getName());
                                }
                                break;
                            }
                        }
                        map.put(tableName.value(), Collections.singletonList(tableIdVale));
                    }
                }
            }
            return map;
        } else {
            return null;
        }
    }

    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
