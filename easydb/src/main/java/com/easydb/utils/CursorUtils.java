package com.easydb.utils;

import android.database.Cursor;


import com.easydb.table.ColumnInfo;
import com.easydb.table.TableInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwy on 2017/4/27.
 */

public class CursorUtils {
    /**
     * 根据类型T，生产其实体对象
     * @param cursor
     * @param clazz
     * @param columns
     * @param <T>
     * @return
     */
    public static <T> T getObjectByCursor(Cursor cursor, Class clazz, List<ColumnInfo> columns)throws InstantiationException,IllegalAccessException{
     T obj  = (T) clazz.newInstance();
     for (ColumnInfo column : columns) {
         String value = cursor.getString(cursor.getColumnIndex(column.getColumn()));
         column.setValue(obj,value);
     }
     return obj;
    }

    /**
     * 根据cursor和clazz生产其实例
     * @param cursor
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObjectByCursor(Cursor cursor,Class clazz)throws InstantiationException,IllegalAccessException{
        T obj = null;
        if (cursor.moveToNext()) {
            TableInfo table = TableInfo.get(clazz);
            obj = (T)clazz.newInstance();
            List<ColumnInfo> columns = table.getColumns();
            columns.add(table.idInfo);
            for (ColumnInfo column : columns) {
                String value = cursor.getString(cursor.getColumnIndex(column.getColumn()));
                column.setValue(obj,value);
            }
        }
        return  obj;
    }

    /**
     * 根据表信息和cursor生产数据集合
     * @param cursor        游标
     * @param clazz         数据对象类型
     * @param <T>            对象泛型
     * @return
     */
    public static <T> List<T> getListByCursor(Cursor cursor, Class clazz){
        TableInfo tableInfo = TableInfo.get(clazz);
        List<T> dataList = new ArrayList<T>();
        List<ColumnInfo> columns = tableInfo.getColumns();
        if (tableInfo.idInfo != null)columns.add(tableInfo.idInfo);
        try {
            while (cursor.moveToNext()) {
                T t = CursorUtils.getObjectByCursor(cursor,tableInfo.getClazz(),columns);
                dataList.add(t);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return dataList;
    }

}
