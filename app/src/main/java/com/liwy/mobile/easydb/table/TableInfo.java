package com.liwy.mobile.easydb.table;

import com.liwy.mobile.easydb.EasyDB;
import com.liwy.mobile.easydb.annotation.Column;
import com.liwy.mobile.easydb.annotation.Id;
import com.liwy.mobile.easydb.utils.ClassUtils;
import com.liwy.mobile.easydb.utils.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwy on 2017/4/18.
 */

public class TableInfo {
//    public String className;        // 类名
    public String tableName;        // 表明（Table注解）
    public Object obj;               // 实体类
    public IdInfo idInfo;                      // 表的id主键
    public Class clazz;
    public List<KeyValue> keyValues;
    public List<Field> fields;
    public List<ColumnInfo> columns;

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
        tableInfo.tableName = ClassUtils.getTableName(clazz);
        //获取requiore不为false的参数
       Field[] fields = clazz.getDeclaredFields();//该类所有的属性
        List<Field> classFields = FieldUtils.getClassFields(clazz);
       tableInfo.columns = new ArrayList<ColumnInfo>();
        tableInfo.fields = new ArrayList<Field>();
        for (Field field : classFields){
            Annotation[] annotations = field.getDeclaredAnnotations();
            String columnName = "";
            boolean required = true;
            if (annotations == null || annotations.length == 0){
                ColumnInfo columnInfo = getColumnByField(clazz,field);
                if ("".equals(columnName))columnName = field.getName();
                columnInfo.setColumn(columnName);
                tableInfo.columns.add(columnInfo);
                tableInfo.fields.add(field);
            }else{
                for (Annotation annotation : annotations){
                    if (annotation instanceof Column){
                        Column column = (Column)annotation;
                        columnName = column.value();
                        required = column.require();
                        if (required){
                            ColumnInfo columnInfo = getColumnByField(clazz,field);
                            if ("".equals(columnName))columnName = field.getName();
                            columnInfo.setColumn(columnName);
                            tableInfo.columns.add(columnInfo);
                            tableInfo.fields.add(field);
                            break;
                        }
                    }else if (annotation instanceof Id){
                        Id id = (Id)annotation;
                        tableInfo.idInfo  = (IdInfo) getIdByField(clazz,field);
                        String idName = id.value();
                        if ("".equals(idName))idName = field.getName();
                        tableInfo.idInfo.setColumn(idName);
                        tableInfo.fields.add(field);
                        break;
                    }
                }
            }
        }

        return tableInfo;
    }
    /**
     * 生成TableInfo信息
     * @param entity
     * @return
     */
    public static TableInfo get(Object entity){
        Class clazz = entity.getClass();
        TableInfo tableInfo = new TableInfo(entity);
        tableInfo.tableName = ClassUtils.getTableName(clazz);
        //获取requiore不为false的参数
        Field[] fields = clazz.getDeclaredFields();//该类所有的属性
        List<Field> classFields = FieldUtils.getClassFields(clazz);
        tableInfo.columns = new ArrayList<ColumnInfo>();
        tableInfo.fields = new ArrayList<Field>();
        tableInfo.keyValues = new ArrayList<KeyValue>();
        for (Field field : classFields){
            Annotation[] annotations = field.getDeclaredAnnotations();
            boolean required = true;
            if (annotations == null || annotations.length == 0){
                ColumnInfo columnInfo = getColumnByField(clazz,field);
                columnInfo.setColumn( field.getName());
                tableInfo.columns.add(columnInfo);
                tableInfo.fields.add(field);
                tableInfo.keyValues.add(FieldUtils.getKeyValue(entity,field,field.getName()));
                continue;
            }else {
                for (Annotation annotation : annotations){
                    if (annotation instanceof Column){
                        Column column = (Column)annotation;
                        String columnName  = column.value();
                        required = column.require();
                        if (required){
                            ColumnInfo columnInfo = getColumnByField(clazz,field);
                            if ("".equals(columnName))columnName = field.getName();
                            columnInfo.setColumn(columnName);
                            tableInfo.columns.add(columnInfo);
                            tableInfo.fields.add(field);
                            tableInfo.keyValues.add(FieldUtils.getKeyValue(entity,field,columnName));
                            break;
                        }
                    }else if (annotation instanceof Id){
                        Id id = (Id)annotation;
                        tableInfo.idInfo  = getIdByField(clazz,field);
                        String idName = id.value();
                        if ("".equals(idName))idName = field.getName();
                        tableInfo.idInfo.setColumn(idName);
                        tableInfo.fields.add(field);
                        tableInfo.keyValues.add(FieldUtils.getKeyValue(entity,field,idName));
                        break;
                    }
                }
            }

        }

        return tableInfo;
    }


    /**
     * 生产表的列信息
     * @param clazz
     * @param field
     * @return
     */
    public static ColumnInfo getColumnByField(Class clazz,Field field){
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setField(field);
        columnInfo.setGet(FieldUtils.getGetMethodByField(clazz,field));
        columnInfo.setSet(FieldUtils.getSetMethodByField(clazz,field));
        columnInfo.setDataType(field.getType());
        return columnInfo;
    }

    /**
     * 生产表的Id信息
     * @param clazz
     * @param field
     * @return
     */
    public static IdInfo getIdByField(Class clazz,Field field){
        IdInfo idInfo = new IdInfo();
        idInfo.setField(field);
        idInfo.setGet(FieldUtils.getGetMethodByField(clazz,field));
        idInfo.setSet(FieldUtils.getSetMethodByField(clazz,field));
        idInfo.setDataType(field.getType());
        return idInfo;
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

    public IdInfo getIdInfo() {
        return idInfo;
    }

    public void setIdInfo(IdInfo idInfo) {
        this.idInfo = idInfo;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
