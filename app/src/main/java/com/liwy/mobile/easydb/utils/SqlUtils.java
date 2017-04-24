package com.liwy.mobile.easydb.utils;

import com.liwy.mobile.easydb.EasyDB;
import com.liwy.mobile.easydb.table.ColumnInfo;
import com.liwy.mobile.easydb.table.IdInfo;
import com.liwy.mobile.easydb.table.KeyValue;
import com.liwy.mobile.easydb.table.SqlInfo;
import com.liwy.mobile.easydb.table.TableInfo;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */

public class SqlUtils {

    public static String createTable(Class clazz){
        TableInfo tableInfo = TableInfo.get(clazz);
        String tableName = tableInfo.getTableName();

        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists " + tableName);
        sb.append(" ( ");

        if (tableInfo.getIdInfo() != null){
            IdInfo idInfo = tableInfo.getIdInfo();
            if (idInfo.getDataType() == Integer.TYPE || idInfo.getDataType() == Integer.class){
                sb.append("\"").append(idInfo.getColumn()).append("\" ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
            }else if (idInfo.getDataType() == String.class){
                sb.append("\"").append(idInfo.getColumn()).append("\" ").append("TEXT PRIMARY KEY,");
            }
        }
        List<ColumnInfo> columnInfos = tableInfo.getColumns();
        for (ColumnInfo column : columnInfos){
            sb.append("\"").append(column.getColumn()).append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }

    /**
     * 插入语句
     * @param entity
     * @return
     */
    public static SqlInfo insert(Object entity){
        TableInfo tableInfo = TableInfo.get(entity);
        List<KeyValue> keyValues = tableInfo.getKeyValues();
        if (keyValues.size() > 0) {
            StringBuffer sql = new StringBuffer("insert into " + ClassUtils.getTableName(entity.getClass()) + "(");
            StringBuffer values = new StringBuffer(" values(");
            SqlInfo sqlInfo = new SqlInfo();
            for (KeyValue keyValue : keyValues) {
                sql.append(keyValue.getKey() + ",");
                values.append("?,");
                sqlInfo.addValue(keyValue.getValue());
            }
            values.deleteCharAt(values.length() - 1);
            sql.deleteCharAt(sql.length() - 1).append(")").append(values.append(")"));
            sqlInfo.setSql(sql.toString());
            return sqlInfo;
        }
        return null;
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
