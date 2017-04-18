package com.liwy.mobile.easydb.annotation.utils;

import com.liwy.mobile.easydb.annotation.Table;

/**
 * Created by admin on 2017/4/18.
 */

public class ClassUtils {
    // 获取表名
    public static String getTableName(Class clazz){
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation == null || annotation.value().trim().length() == 0){
            return clazz.getName().replace(".","_");
        }
        return annotation.value();
    }
}
