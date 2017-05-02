package com.easydb.table;


import com.easydb.annotation.Column;
import com.easydb.annotation.Exclusion;
import com.easydb.annotation.Id;
import com.easydb.annotation.ManyToOne;
import com.easydb.annotation.OneToMany;
import com.easydb.utils.ClassUtils;
import com.easydb.utils.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.easydb.table.ColumnInfo.getColumnByField;
import static com.easydb.table.IdInfo.getIdByField;
/**
 * 表信息类
 * Created by liwy on 2017/4/18.
 */
public class TableInfo {
    public String tableName;        // 表明（Table注解）
    public Object obj;               // 实体类
    public IdInfo idInfo;                      // 表的id主键
    public Class clazz;
    public List<KeyValue> keyValues;
    public List<Field> fields;
    public List<ColumnInfo> columns;
    private boolean checkDatabese;
    public HashMap<String, OneToManyInfo> otms;// 一对多的属性集合
    public HashMap<String, ManyToOneInfo> mtos;// 多对一的属性集合
    private HashMap<String, TableInfo> tableInfoHashMap;//缓存类信息

    public TableInfo(Class clazz) {
        this.clazz = clazz;
    }

    public TableInfo(Object obj) {
        this.obj = obj;
        this.clazz = obj.getClass();
    }

    /**
     * 生成TableInfo信息
     *
     * @param clazz
     * @return
     */
    public static TableInfo get(Class clazz) {
        TableInfo tableInfo = new TableInfo(clazz);
        tableInfo.tableName = ClassUtils.getTableName(clazz);
        //获取requiore不为false的参数
        List<Field> classFields = FieldUtils.getClassFields(clazz);
        tableInfo.columns = new ArrayList<ColumnInfo>();
        tableInfo.fields = new ArrayList<Field>();
        tableInfo.otms = new HashMap<String, OneToManyInfo>();
        tableInfo.mtos = new HashMap<String, ManyToOneInfo>();
        for (Field field : classFields) {
            //存在排除注解（Exclusion）时，则该属性不作为表字段
            Exclusion exclusion = field.getAnnotation(Exclusion.class);
            if (exclusion != null) continue;
            String columnName = "";
            boolean required = true;
            Annotation[] annotations = field.getDeclaredAnnotations();
            // 当注解为空即默认情况下，加入表字段
            if (annotations == null || annotations.length == 0) {
                ColumnInfo columnInfo = getColumnByField(clazz, field);
                if ("".equals(columnName)) columnName = field.getName();
                columnInfo.setColumn(columnName);
                tableInfo.columns.add(columnInfo);
                tableInfo.fields.add(field);
            } else {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Column) {
                        Column column = (Column) annotation;
                        columnName = column.value();
                        ColumnInfo columnInfo = getColumnByField(clazz, field);
                        if ("".equals(columnName)) columnName = field.getName();
                        columnInfo.setColumn(columnName);
                        tableInfo.columns.add(columnInfo);
                        tableInfo.fields.add(field);
                        break;
                    } else if (annotation instanceof Id) {
                        Id id = (Id) annotation;
                        tableInfo.idInfo = (IdInfo) getIdByField(clazz, field);
                        String idName = id.value();
                        if ("".equals(idName)) idName = field.getName();
                        tableInfo.idInfo.setColumn(idName);
                        tableInfo.fields.add(field);
                        break;
                    } else if (annotation instanceof OneToMany) {
                        OneToMany oneToMany = (OneToMany) annotation;
                        OneToManyInfo otminfo = OneToManyInfo.getOneToManyColumnByField(field, clazz);
                        otminfo.setAssociateColumn(oneToMany.associatedColumn());
                        tableInfo.otms.put(otminfo.getAssociateColumn(), otminfo);
                        tableInfo.otms.put(otminfo.getColumn(), otminfo);
                    } else if (annotation instanceof ManyToOne) {
                        ManyToOne manyToOne = (ManyToOne) annotation;
                        ManyToOneInfo manyToOneInfo = ManyToOneInfo.getManyToOneByField(clazz, field);
                        String mtoColumnName = manyToOne.associatedColumn();
                        if ("".equals(mtoColumnName)) mtoColumnName = field.getName();
                        manyToOneInfo.setColumn(mtoColumnName);
                        tableInfo.mtos.put(mtoColumnName, manyToOneInfo);
                    }
                }
            }
        }

        return tableInfo;
    }

    /**
     * 生成TableInfo信息
     *
     * @param entity
     * @return
     */
    public static TableInfo get(Object entity) {
        Class clazz = entity.getClass();
        TableInfo tableInfo = new TableInfo(entity);
        tableInfo.tableName = ClassUtils.getTableName(clazz);
        //获取requiore不为false的参数
        Field[] fields = clazz.getDeclaredFields();//该类所有的属性
        List<Field> classFields = FieldUtils.getClassFields(clazz);
        tableInfo.columns = new ArrayList<ColumnInfo>();
        tableInfo.fields = new ArrayList<Field>();
        tableInfo.keyValues = new ArrayList<KeyValue>();
        tableInfo.otms = new HashMap<String, OneToManyInfo>();
        tableInfo.mtos = new HashMap<String, ManyToOneInfo>();
        for (Field field : classFields) {
            //存在排除注解（Exclusion）时，则该属性不作为表字段
            Exclusion exclusion = field.getAnnotation(Exclusion.class);
            if (exclusion != null) continue;
            Annotation[] annotations = field.getDeclaredAnnotations();
            // 当注解为空即默认情况下，加入表字段
            if (annotations == null || annotations.length == 0) {
                ColumnInfo columnInfo = getColumnByField(clazz, field);
                columnInfo.setColumn(field.getName());
                tableInfo.columns.add(columnInfo);
                tableInfo.fields.add(field);
                tableInfo.keyValues.add(FieldUtils.getKeyValue(entity, field, field.getName()));
                continue;
            } else {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Column) {
                        Column column = (Column) annotation;
                        String columnName = column.value();
                        ColumnInfo columnInfo = getColumnByField(clazz, field);
                        if ("".equals(columnName)) columnName = field.getName();
                        columnInfo.setColumn(columnName);
                        tableInfo.columns.add(columnInfo);
                        tableInfo.fields.add(field);
                        tableInfo.keyValues.add(FieldUtils.getKeyValue(entity, field, columnName));
                        break;
                    } else if (annotation instanceof Id) {
                        Id id = (Id) annotation;
                        tableInfo.idInfo = getIdByField(clazz, field);
                        String idName = id.value();
                        if ("".equals(idName)) idName = field.getName();
                        tableInfo.idInfo.setColumn(idName);
                        tableInfo.fields.add(field);
                        tableInfo.keyValues.add(FieldUtils.getKeyValue(entity, field, idName));
                        break;
                    } else if (annotation instanceof OneToMany) {
                        OneToMany oneToMany = (OneToMany) annotation;
                        OneToManyInfo otminfo = OneToManyInfo.getOneToManyColumnByField(field, clazz);
                        otminfo.setAssociateColumn(oneToMany.associatedColumn());
                        tableInfo.otms.put(otminfo.getAssociateColumn(), otminfo);
                    } else if (annotation instanceof ManyToOne) {
                        ManyToOne manyToOne = (ManyToOne) annotation;
                        ManyToOneInfo manyToOneInfo = ManyToOneInfo.getManyToOneByField(clazz, field);
                        String mtoColumnName = manyToOne.associatedColumn();
                        if ("".equals(mtoColumnName)) mtoColumnName = field.getName();
                        manyToOneInfo.setColumn(mtoColumnName);
                        tableInfo.mtos.put(mtoColumnName, manyToOneInfo);
                        tableInfo.keyValues.add(manyToOne2KeyValue(manyToOneInfo,entity));
                    }
                }
            }

        }

        return tableInfo;
    }

    /**
     * 将ManyToOneInfo的键值对解析成KeyValue
     * @param many
     * @param entity
     * @return
     */
    private static KeyValue manyToOne2KeyValue(ManyToOneInfo many, Object entity) {
        KeyValue kv = null;
        String manycolumn = many.getColumn();
        Object manyobject = many.getValue(entity);
        if (manyobject != null) {
            Object manyvalue;
//            Object manyvalue;
//            if (manyobject.getClass() == ManyToOneLazyLoader.class) {
//                manyvalue = TableInfo.get(many.getManyClass()).getId().getValue(manyobject);
//            } else {
            manyvalue = TableInfo.get(manyobject.getClass()).getIdInfo().getValue(manyobject);//获取ManyToOne关系中父类的id值
//            }
            if ((manycolumn != null) && (manyvalue != null)) {
                kv = new KeyValue(manycolumn, manyvalue);
            }
        }
        return kv;
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

    public HashMap<String, OneToManyInfo> getOtms() {
        return otms;
    }

    public void setOtms(HashMap<String, OneToManyInfo> otms) {
        this.otms = otms;
    }

    public HashMap<String, ManyToOneInfo> getMtos() {
        return mtos;
    }

    public void setMtos(HashMap<String, ManyToOneInfo> mtos) {
        this.mtos = mtos;
    }

    public boolean isCheckDatabese() {
        return checkDatabese;
    }

    public void setCheckDatabese(boolean checkDatabese) {
        this.checkDatabese = checkDatabese;
    }
}
