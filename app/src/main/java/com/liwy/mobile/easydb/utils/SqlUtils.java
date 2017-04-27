package com.liwy.mobile.easydb.utils;

import com.liwy.mobile.easydb.EasyDB;
import com.liwy.mobile.easydb.table.ColumnInfo;
import com.liwy.mobile.easydb.table.IdInfo;
import com.liwy.mobile.easydb.table.KeyValue;
import com.liwy.mobile.easydb.table.SqlInfo;
import com.liwy.mobile.easydb.table.TableInfo;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */

public class SqlUtils {

    /**
     * 创建表语句
     * @param clazz
     * @return
     */
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
     * 清空表数据语句
     * @param clazz
     * @return
     */
    public static String deleteAll(Class clazz){
        return "delete table " + ClassUtils.getTableName(clazz);
    }

    /**
     * 根据表id删除数据
     * @param entity
     * @return
     */
    public static String deleteById(Object entity){
        TableInfo tableInfo = TableInfo.get(entity);
        if (tableInfo.idInfo == null)return null;
        StringBuffer sql = new StringBuffer();
        if (tableInfo.idInfo.getDataType() == Integer.class || tableInfo.idInfo.getDataType() == Integer.TYPE){
            sql.append("delete from ").append(ClassUtils.getTableName(entity.getClass())).append(" where ").append(tableInfo.getIdInfo().getColumn()).append(" = ").append((tableInfo.getIdInfo().getValue(entity)));
        }else{
            sql.append("delete from ").append(ClassUtils.getTableName(entity.getClass())).append(" where ")
                    .append(tableInfo.getIdInfo().getColumn()).append(" = \"").append((tableInfo.getIdInfo().getValue(entity)))
            .append("\"");
        }
        return sql.toString();
    }

    /**
     * 删除表语句
     * @param clazz
     * @return
     */
    public static String drop(Class clazz){
        return "DROP TABLE IF EXISTS " + ClassUtils.getTableName(clazz);
    }

    /**
     * 生产查询语句（全表查询）
     * @param clazz
     * @return
     */
    public static String findAll(Class clazz){
        return "select * from " + ClassUtils.getTableName(clazz);
    }

    /**
     * 生产查询语句（根据id查询）
     * @param entity
     * @return
     */
    public static SqlInfo findById(Object entity){
        TableInfo table = TableInfo.get(entity);
        if (table.getIdInfo() == null)return null;
        SqlInfo sqlInfo = new SqlInfo();

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ").append(ClassUtils.getTableName(entity.getClass())).append(" where ").append(table.getIdInfo().getColumn()).append(" = ?");
        sqlInfo.setSql(sql.toString());
        sqlInfo.addValue(table.getIdInfo().getValue(entity));
        return sqlInfo;
    }

    /**
     * 根据搜索条件查询数据
     * @param clazz
     * @param strCondition  查询条件
     * @return
     */
    public static SqlInfo findByCondition(Class clazz,String strCondition){
        StringBuffer sb = new StringBuffer(findAll(clazz)).append(" ").append(strCondition);
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(sb.toString());
        return sqlInfo;
    }


    /**
     * 根据id更新数据语句
     * @param entity
     * @return
     */
    public static SqlInfo updateById(Object entity){
        TableInfo table = TableInfo.get(entity);
        if (table.idInfo == null)return null;
        SqlInfo sqlInfo = getUpdateBaseSQL(table);
        StringBuffer sql = new StringBuffer(sqlInfo.getSql());
        sql.append(" where ").append(table.getIdInfo().getColumn()).append(" = ?");
        Object obj = FieldUtils.getFieldValue(entity,table.getIdInfo().getField());
        sqlInfo.addValue(obj);
        sqlInfo.setSql(sql.toString());
        return sqlInfo;
    }

    /**
     * 根据条件更新语句
     * @param entity
     * @param strCondition
     * @return
     */
    public static SqlInfo updateByCondition(Object entity,String strCondition){
        TableInfo table = TableInfo.get(entity);
        if (table.idInfo == null)return null;
        SqlInfo sqlInfo = getUpdateBaseSQL(table);
        StringBuffer sql = new StringBuffer(sqlInfo.getSql());
        sql.append(" ").append(strCondition);
        sqlInfo.setSql(sql.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateBaseSQL(TableInfo table){
        SqlInfo sqlInfo = new SqlInfo();
        List<KeyValue> keyValues = table.getKeyValues();
        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(table.getTableName()).append(" set ");
        for (KeyValue keyValue : keyValues){
            if (keyValue.getKey().equals(table.getIdInfo().getColumn()))continue;
            sql.append(keyValue.getKey()).append(" = ?,");
            sqlInfo.addValue(keyValue.getValue());
        }
        sqlInfo.setSql(sql.deleteCharAt(sql.length() - 1).toString());
        return sqlInfo;
    }


}
