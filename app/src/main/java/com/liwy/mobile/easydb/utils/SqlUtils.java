package com.liwy.mobile.easydb.utils;

import com.liwy.mobile.easydb.table.KeyValue;
import com.liwy.mobile.easydb.table.SqlInfo;
import com.liwy.mobile.easydb.table.TableInfo;

import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */

public class SqlUtils {

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
