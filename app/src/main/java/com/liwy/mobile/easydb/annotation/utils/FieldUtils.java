package com.liwy.mobile.easydb.annotation.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by admin on 2017/4/18.
 */

public class FieldUtils {

    public static Field getFieldByName(Class clazz,String fieldName){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static Method getMethodByFieldName(Class clazz,String fieldName){
//        String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
//        try {
//            return clazz.getDeclaredMethod(methodName,new Class[0]);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * 通过属性获取get方法
     * @param clazz
     * @param field
     * @return
     */
    public static Method getMethodByField(Class clazz, Field field){
        String fieldName = field.getName();
        String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        try {
            clazz.getDeclaredMethod(methodName,new Class[]{field.getType()});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过属性名获取get方法
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method getMethodByFieldName(Class clazz, String fieldName){
        try {
            return getMethodByField(clazz,clazz.getDeclaredField(fieldName));
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
     * @param obj
     * @param field
     * @return
     */
    public static Object getFieldValue(Object obj,Field field){
        Method method = getMethodByField(obj.getClass(),field);
        return invoke(obj,method);
    }
    public static Object getFieldValue(Object obj,String fieldName){
        Method method = getMethodByFieldName(obj.getClass(),fieldName);
        return invoke(obj,method);
    }

    /**
     * 设置参数的值
     * @param entity
     * @param field
     * @param value
     */
    public static void setFieldValue(Object entity,Field field,Object value){
        try {
            Method method = getMethodByField(entity.getClass(),field);
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
