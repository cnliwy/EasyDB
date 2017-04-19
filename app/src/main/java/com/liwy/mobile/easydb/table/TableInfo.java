package com.liwy.mobile.easydb.table;

import android.text.TextUtils;

import com.liwy.mobile.easydb.annotation.Column;
import com.liwy.mobile.easydb.annotation.utils.FieldUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by liwy on 2017/4/18.
 */

public class TableInfo {
    public String className;        // 类名
    public String tableName;        // 表明（Table注解）
    public Object obj;               // 实体类
    public Class clazz;
    public List<KeyValue> keyValues;
    public Field[] fields;
    public List<FieldInfo> fieldInfos;

    public TableInfo(Class clazz) {
        this.clazz = clazz;
    }

    public TableInfo(Object obj) {
        this.obj = obj;
        this.clazz = obj.getClass();
    }

    /**
     * 生成TableInfo信息
     * @param clazz
     * @return
     */
    public static TableInfo get(Class clazz){
        TableInfo tableInfo = new TableInfo(clazz);
        //获取requiore不为false的参数
       Field[] fields = clazz.getDeclaredFields();//该类所有的属性
        List<Field> classFields = FieldUtils.getClassFields(clazz);
        List<Field> realfieldList = new ArrayList<Field>();
        for (Field field : classFields){
            Annotation[] annotations = field.getDeclaredAnnotations();
            String columnName = "";
            boolean required = true;
            for (Annotation annotation : annotations){
                if (annotation instanceof Column){
                    Column column = (Column)annotation;
                    columnName = column.value();
                    required = column.require();
                    if (required){
                        realfieldList.add(field);
                        break;
                    }

                }
            }
        }
        int count = realfieldList.size();
        if (count > 0){
            tableInfo.fields = new Field[realfieldList.size()];
            for (int i = 0; i < count; i++){
                Field field = realfieldList.get(i);
                tableInfo.fields[i] = field;
            }
        }
        return tableInfo;
    }

    /**
     * 获取用于数据库操作的键值对
     * @return
     */
    public static List<KeyValue> getKeyValues(Object obj){
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<Field> fields = FieldUtils.getClassFields(obj.getClass());
        if (fields.size() > 0){
            for (Field field : fields){
                Annotation[] annotations = field.getDeclaredAnnotations();
                boolean isRealColumn = true;
                String columnName = "";
                for (Annotation annotation : annotations){
                    if (annotation instanceof Column){
                        Column column = (Column)annotation;
                        if (column.require()){
                            columnName = column.value();
                            if (TextUtils.isEmpty(columnName)){
                                columnName = field.getName();
                            }
                        }else{
                            isRealColumn = false;
                        }
                        break;
                    }
                }
                // 除了被标注为required=false的情况下，其余字段均为表字段
                if (isRealColumn){
                    try {
                        if (!field.isAccessible())field.setAccessible(true);
                        if (TextUtils.isEmpty(columnName)){
                            columnName = field.getName();
                        }
                        KeyValue keyValue = new KeyValue(columnName,field.get(obj));
                        keyValues.add(keyValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return keyValues;
    }




    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<KeyValue> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(List<KeyValue> keyValues) {
        this.keyValues = keyValues;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
