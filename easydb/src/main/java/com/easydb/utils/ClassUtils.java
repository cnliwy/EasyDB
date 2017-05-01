package com.easydb.utils;


import com.easydb.annotation.Table;

/**
 * Created by admin on 2017/4/18.
 */

public class ClassUtils {
    // 获取表名
    public static String getTableName(Class clazz){
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation == null || annotation.value().trim().length() == 0){
            return clazz.getName().replace(".","_");
        }
        return annotation.value();
    }

    /**
     * 获取用于数据库操作的键值对
     * @return
     */
//    public static List<KeyValue> getKeyValues(Object obj){
//        List<KeyValue> keyValues = new ArrayList<KeyValue>();
//        List<Field> fields = FieldUtils.getClassFields(obj.getClass());
//        if (fields.size() > 0){
//            for (Field field : fields){
//                Annotation[] annotations = field.getDeclaredAnnotations();
//                boolean isRealColumn = true;
//                String columnName = "";
//                for (Annotation annotation : annotations){
//                    if (annotation instanceof Column){
//                        Column column = (Column)annotation;
//                        if (column.require()){
//                            columnName = column.value();
//                            if (TextUtils.isEmpty(columnName)){
//                                columnName = field.getName();
//                            }
//                        }else{
//                            isRealColumn = false;
//                        }
//                        break;
//                    }
//                }
//                // 除了被标注为required=false的情况下，其余字段均为表字段
//                if (isRealColumn){
//                    try {
//                        if (!field.isAccessible())field.setAccessible(true);
//                        if (TextUtils.isEmpty(columnName)){
//                            columnName = field.getName();
//                        }
//                        KeyValue keyValue = new KeyValue(columnName,field.get(obj));
//                        keyValues.add(keyValue);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }
//        return keyValues;
//    }
}
