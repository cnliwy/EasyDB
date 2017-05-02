package com.easydb.table;

import com.easydb.utils.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 一对多关系信息类
 * Created by liwy on 2017/4/28.
 */

public class OneToManyInfo extends ColumnInfo {
    /**
     * 集合泛型里的数据类型
     */
    private Class oneClass;

    private String associateColumn;

    /**
     * 获取OneToMany注解
     * @param field
     * @return
     */
    public static OneToManyInfo getOneToManyColumnByField(Field field,Class clazz){
        OneToManyInfo oneToManyInfo = new OneToManyInfo();
        oneToManyInfo.setColumn(FieldUtils.getColumnByField(field));
        oneToManyInfo.setGet(FieldUtils.getGetMethodByField(clazz,field));
        oneToManyInfo.setSet(FieldUtils.getSetMethodByField(clazz,field));
        Type type = field.getGenericType();
        if ((type instanceof ParameterizedType))
        {
            ParameterizedType pType = (ParameterizedType)field.getGenericType();
            if (pType.getActualTypeArguments().length == 1)
            {
                Class<?> pClazz = (Class)pType.getActualTypeArguments()[0];
                if (pClazz != null) {
                    oneToManyInfo.setOneClass(pClazz);
                }
            }
            else
            {
                Class<?> pClazz = (Class)pType.getActualTypeArguments()[1];
                if (pClazz != null) {
                    oneToManyInfo.setOneClass(pClazz);
                }
            }
        }
        return oneToManyInfo;
    }

    public Class getOneClass() {
        return oneClass;
    }

    public void setOneClass(Class oneClass) {
        this.oneClass = oneClass;
    }

    public String getAssociateColumn() {
        return associateColumn;
    }

    public void setAssociateColumn(String associateColumn) {
        this.associateColumn = associateColumn;
    }
}
