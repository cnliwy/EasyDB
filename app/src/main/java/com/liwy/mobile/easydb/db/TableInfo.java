package com.liwy.mobile.easydb.db;

import com.liwy.mobile.easydb.annotation.Table;
import com.orhanobut.logger.Logger;

import java.lang.annotation.Annotation;

/**
 * Created by liwy on 2017/4/18.
 */

public class TableInfo {
    public Class clazz;

    public TableInfo(Class clazz) {
        this.clazz = clazz;
    }

    // 获取表名
    public  String getTableName(){
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation == null || annotation.value().trim().length() == 0){
           return clazz.getName().replace(".","_");
        }
        return annotation.value();
    }
}
