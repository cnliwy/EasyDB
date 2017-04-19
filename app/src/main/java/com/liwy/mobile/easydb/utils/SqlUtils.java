package com.liwy.mobile.easydb.utils;

/**
 * Created by admin on 2017/4/19.
 */

public class SqlUtils {

    public static String insert(){
        return "";
    }

    /**
     * 删除表语句
     * @param clazz
     * @return
     */
    public static String dropTable(Class clazz){
        return "delete table " + ClassUtils.getTableName(clazz);
    }
}
