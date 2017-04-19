package com.liwy.mobile.easydb.utils;

import com.liwy.mobile.easydb.table.KeyValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
                Class<?> type = field.getType();
                if (type == String.class){
                    method.invoke(entity,new Object[]{value.toString()});
                }else if (type == Integer.TYPE || type == Integer.class){
                    method.invoke(entity,new Object[]{Integer.valueOf(value == null ? 0 : Integer.parseInt(value.toString()))});
                }else{
                    method.invoke(entity,new Object[0]);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




}
