package com.easydb.table;


import com.easydb.utils.FieldUtils;

import java.lang.reflect.Field;

/**
 * id字段信息
 * Created by liwy on 2017/4/22.
 */

public class IdInfo extends ColumnInfo {

    /**
     * 生产表的Id信息
     * @param clazz
     * @param field
     * @return
     */
    public static IdInfo getIdByField(Class clazz,Field field){
        IdInfo idInfo = new IdInfo();
        idInfo.setField(field);
        Class<?> type = field.getType();
        idInfo.setDataType(type);
        idInfo.setGet(FieldUtils.getGetMethodByField(clazz,field));
        idInfo.setSet(FieldUtils.getSetMethodByField(clazz,field));
        return idInfo;
    }
}
