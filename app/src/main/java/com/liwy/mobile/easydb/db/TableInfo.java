package com.liwy.mobile.easydb.db;

import com.liwy.mobile.easydb.annotation.Table;
import com.orhanobut.logger.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by liwy on 2017/4/18.
 */

public class TableInfo {
    public Class clazz;

    public TableInfo(Class clazz) {
        this.clazz = clazz;
    }



    // test
    public static List<String> parseParms(Class clazz){
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0){
            for (Field field : fields){
                Logger.d(field.getName());
            }
        }
        return null;
    }
}
