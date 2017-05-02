package com.easydb.table;


import com.easydb.utils.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.easydb.utils.Utils.stringToDateTime;

/**
 * 表字段信息
 * Created by liwy on 2017/4/18.
 */

public class ColumnInfo {
    private String fieldName;
    private String column;
    private Class<?> dataType;
    private Field field;
    private Method set;
    private Method get;

    public  <T> T getValue(Object obj){
        if (obj == null || this.get == null){
            return null;
        }
        try {
            return (T) this.get.invoke(obj,new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void setValue(Object receiver, Object value)
    {
        if ((this.set != null) && (value != null)) {
            try
            {
                if (this.dataType == String.class) {
                    this.set.invoke(receiver, new Object[] { value.toString() });
                } else if ((this.dataType == Integer.TYPE) || (this.dataType == Integer.class)) {
                    this.set.invoke(receiver, new Object[] { Integer.valueOf(value == null ? 0 : Integer.parseInt(value.toString())) });
                } else if ((this.dataType == Float.TYPE) || (this.dataType == Float.class)) {
                    this.set.invoke(receiver, new Object[] { Float.valueOf(value == null ? 0 : Float.parseFloat(value.toString())) });
                } else if ((this.dataType == Double.TYPE) || (this.dataType == Double.class)) {
                    this.set.invoke(receiver, new Object[] { Double.valueOf(value == null ? 0 : Double.parseDouble(value.toString())) });
                } else if ((this.dataType == Long.TYPE) || (this.dataType == Long.class)) {
                    this.set.invoke(receiver, new Object[] { Long.valueOf(value == null ? 0 : Long.parseLong(value.toString())) });
                } else if ((this.dataType == java.util.Date.class) || (this.dataType == java.sql.Date.class)) {
                    this.set.invoke(receiver, new Object[] { value == null ? null : stringToDateTime(value.toString()) });
                } else if ((this.dataType == Boolean.TYPE) || (this.dataType == Boolean.class)) {
                    this.set.invoke(receiver, new Object[] { Boolean.valueOf(value == null ? false : "1".equals(value.toString())) });
                } else {
                    this.set.invoke(receiver, new Object[] { value });
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } else {
            try
            {
                this.field.setAccessible(true);
                this.field.set(receiver, value);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生产表的列信息
     * @param clazz
     * @param field
     * @return
     */
    public static ColumnInfo getColumnByField(Class clazz,Field field){
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setField(field);
        Class<?> type = field.getType();
        columnInfo.setDataType(type);
        columnInfo.setGet(FieldUtils.getGetMethodByField(clazz,field));
        columnInfo.setSet(FieldUtils.getSetMethodByField(clazz,field));
        return columnInfo;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Method getSet() {
        return set;
    }

    public void setSet(Method set) {
        this.set = set;
    }

    public Method getGet() {
        return get;
    }

    public void setGet(Method get) {
        this.get = get;
    }
}
