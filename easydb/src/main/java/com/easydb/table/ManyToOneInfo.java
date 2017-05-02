package com.easydb.table;

import com.easydb.utils.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by liwy on 2017/5/2.
 */

public class ManyToOneInfo extends ColumnInfo {

    /**
     * 生产表的MantToOne列信息
     * @param clazz
     * @param field
     * @return
     */
    public static ManyToOneInfo getManyToOneByField(Class clazz,Field field){
        ManyToOneInfo columnInfo = new ManyToOneInfo();
        columnInfo.setField(field);
        Class<?> type = field.getType();
        columnInfo.setDataType(type);
        columnInfo.setGet(FieldUtils.getGetMethodByField(clazz,field));
        columnInfo.setSet(FieldUtils.getSetMethodByField(clazz,field));
        columnInfo.setFieldName(field.getName());
        return columnInfo;
    }
}
