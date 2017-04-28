package com.liwy.mobile.easydb.utils;

import android.util.Property;

import com.liwy.mobile.easydb.annotation.Column;
import com.liwy.mobile.easydb.annotation.Id;
import com.liwy.mobile.easydb.table.ColumnInfo;
import com.liwy.mobile.easydb.table.KeyValue;
import com.liwy.mobile.easydb.table.OneToManyInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;
import static com.liwy.mobile.easydb.utils.Utils.stringToDateTime;

/**
 * Field操作工具类
 * Created by admin on 2017/4/18.
 */

public class FieldUtils {

    /**
     * 根据Class获取该类的字段
     * @param clazz
     * @return
     */
    public static List<Field> getClassFields(Class clazz){
        List<Field> fieldList = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")){
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    /**
     * 根据属性名称获取Field
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getFieldByName(Class clazz,String fieldName){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取该类的属性及值，以作为数据库操作的属性键值对
     * @param object
     * @return
     */
    public static List<KeyValue> getKeyValues(Object object){
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            if(fields.length > 0){
                for (Field field : fields){
                    field.setAccessible(true);
                    KeyValue keyValue = new KeyValue(field.getName(),field.get(object));
                    keyValues.add(keyValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return keyValues;
    }

    /**
     * 根据field获取表字段名称
     * @param field
     * @return
     */
    public static String getColumnByField(Field field)
    {
        Column property = (Column)field.getAnnotation(Column.class);
        if ((property != null) && (property.value().trim().length() != 0)) {
            return property.value();
        }
//        ManyToOne manyToOne = (ManyToOne)field.getAnnotation(ManyToOne.class);
//        if ((manyToOne != null) && (manyToOne.column().trim().length() != 0)) {
//            return manyToOne.column();
//        }
//        OneToMany oneToMany = (OneToMany)field.getAnnotation(OneToMany.class);
//        if ((oneToMany != null) && (oneToMany.manyColumn() != null) && (oneToMany.manyColumn().trim().length() != 0)) {
//            return oneToMany.manyColumn();
//        }
        Id id = (Id)field.getAnnotation(Id.class);
        if ((id != null) && (id.value().trim().length() != 0)) {
            return id.value();
        }
        return field.getName();
    }
    public static Method getBooleanFieldGetMethod(Class<?> clazz, String fieldName)
    {
        String mn = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (isISStart(fieldName)) {
            mn = fieldName;
        }
        try
        {
            return clazz.getDeclaredMethod(mn, new Class[0]);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f)
    {
        String fn = f.getName();
        String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        if (isISStart(f.getName())) {
            mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
        }
        try
        {
            return clazz.getDeclaredMethod(mn, new Class[] { f.getType() });
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过参数名称获取get方法
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method getGetMethodByFieldName(Class clazz,String fieldName){
        String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getDeclaredMethod(methodName,new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getGetMethodByField(Class clazz,Field field){
        if (field.getType() == boolean.class || field.getType() == Boolean.TYPE){
            return getBooleanFieldGetMethod(clazz,field.getName());
        }
        String fieldName = field.getName();
        String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getDeclaredMethod(methodName,new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 通过属性获取set方法
     * @param clazz
     * @param field
     * @return
     */
    public static Method getSetMethodByField(Class clazz, Field field){
        if (field.getType() == Boolean.TYPE || field.getType() == boolean.class){
            return getBooleanFieldSetMethod(clazz,field);
        }
        String fieldName = field.getName();
        String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getDeclaredMethod(methodName,new Class[]{field.getType()});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过属性名获取set方法
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method getSetMethodByFieldName(Class clazz, String fieldName){
        try {
            return getSetMethodByField(clazz,clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否是boolean值的get方法
     * @param fieldName
     * @return
     */
    private static boolean isISStart(String fieldName)
    {
        if ((fieldName == null) || (fieldName.trim().length() == 0)) {
            return false;
        }
        return (fieldName.startsWith("is")) && (!Character.isLowerCase(fieldName.charAt(2)));
    }

    public static Object invoke(Object obj,Method method){
        try {
            return method.invoke(obj,new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取参数的值
     * @param obj 实例对象
     * @param field 参数
     * @return
     */
    public static Object getFieldValue(Object obj,Field field){
        Method method = getGetMethodByField(obj.getClass(),field);
        return invoke(obj,method);
    }
    public static Object getFieldValue(Object obj,String fieldName){
        Method method = getGetMethodByFieldName(obj.getClass(),fieldName);
        return invoke(obj,method);
    }

    public static KeyValue getKeyValue(Object obj,Field field,String columnName){
        Object object = getFieldValue(obj,field);
        return new KeyValue(columnName,object);
    }

    /**
     * 设置参数的值
     * @param entity 实例
     * @param field 参数
     * @param value 值
     */
    public static void setFieldValue(Object entity,Field field,Object value){
        try {
            Method method = getSetMethodByField(entity.getClass(),field);
            if (method != null){
                method.setAccessible(true);
                Class<?> type = field.getType();
                if (type == String.class){
                    method.invoke(entity,new Object[]{value.toString()});
                }else if (type == Integer.TYPE || type == Integer.class){
                    method.invoke(entity,new Object[]{Integer.valueOf(value == null ? 0 : Integer.parseInt(value.toString()))});
                }else if ((type == Float.TYPE) || (type == Float.class)) {
                    method.invoke(entity, new Object[] { Float.valueOf(value == null ? 0 : Float.parseFloat(value.toString())) });
                } else if ((type == Double.TYPE) || (type == Double.class)) {
                    method.invoke(entity, new Object[] { Double.valueOf(value == null ? 0 : Double.parseDouble(value.toString())) });
                } else if ((type == Long.TYPE) || (type == Long.class)) {
                    method.invoke(entity, new Object[] { Long.valueOf(value == null ? 0 : Long.parseLong(value.toString())) });
                } else if ((type == java.util.Date.class) || (type == java.sql.Date.class)) {
                    method.invoke(entity, new Object[] { value == null ? null : stringToDateTime(value.toString()) });
                } else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
                    method.invoke(entity, new Object[] { Boolean.valueOf(value == null ? false : "1".equals(value.toString())) });
                } else{
                    method.invoke(entity,new Object[0]);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public static OneToManyInfo getOneToManyColumnByField(Field field){
        OneToManyInfo oneToManyInfo = new OneToManyInfo();
        Type type = field.getGenericType();
        System.out.println(ParameterizedType.class.toString());
        if ((type instanceof ParameterizedType))
        {
            ParameterizedType pType = (ParameterizedType)field.getGenericType();
            System.out.println(pType.toString());
            if (pType.getActualTypeArguments().length == 1)
            {
                Class<?> pClazz = (Class)pType.getActualTypeArguments()[0];
                if (pClazz != null) {
//                        otm.setOneClass(pClazz);
                    System.out.println("[0] class不为空：" + pClazz.getName());
                }
            }
            else
            {
                Class<?> pClazz = (Class)pType.getActualTypeArguments()[1];
                if (pClazz != null) {
                    System.out.println("[1] class不为空：" + pClazz.getName());
                }
            }
        }
        return oneToManyInfo;
    }
}
