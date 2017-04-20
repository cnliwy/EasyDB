package com.liwy.mobile.easydb.utils;

import com.liwy.mobile.easydb.table.KeyValue;
import com.liwy.mobile.easydb.table.SqlInfo;
import com.liwy.mobile.easydb.table.TableInfo;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */

public class SqlUtils {

    public static SqlInfo createTable(Class clazz){
        SqlInfo sqlInfo = new SqlInfo();
        String tableName = ClassUtils.getTableName(clazz);


        Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists " + tableName);
        if (fields.length > 0) {
            sb.append("(");
            for (Field field : fields) {
//                if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                    sb.append( "\"" + field.getName() + "\",");
//                } else if (field.getType() == String.class) {
//                    sb.append(field.getName() + " text,");
//                }
            }
            sb = new StringBuffer(sb.substring(0, sb.length() - 1));
            sb.append(")");
            sqlInfo.setSql(sb.toString());
        }
        return sqlInfo;
    }

    /**
     * 插入语句
     * @param entity
     * @return
     */
    public static SqlInfo insert(Object entity){
        List<KeyValue> keyValues = TableInfo.getKeyValues(entity);
        if (keyValues.size() > 0) {
            StringBuffer sql = new StringBuffer("insert into " + ClassUtils.getTableName(entity.getClass()) + "(");
            StringBuffer values = new StringBuffer(" values(");
            SqlInfo sqlInfo = new SqlInfo();
            for (KeyValue keyValue : keyValues) {
                sql.append(keyValue.getKey() + ",");
                values.append("?,");
                sqlInfo.addObject(keyValue.getValue());
            }
            values = values.deleteCharAt(values.length() - 1);
            sql = sql.deleteCharAt(sql.length() - 1).append(")").append(values.append(")"));
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
