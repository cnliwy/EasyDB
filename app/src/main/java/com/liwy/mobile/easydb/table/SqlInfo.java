package com.liwy.mobile.easydb.table;

import java.util.LinkedList;

/**
 * Created by admin on 2017/4/20.
 */

public class SqlInfo {
    private String sql;
    private LinkedList<Object> dataList;

    public SqlInfo() {
        dataList = new LinkedList<Object>();
    }

    public Object[] getValues(){
        return dataList != null ? dataList.toArray(): null;
    }

    public void addObject(Object obj){
        this.dataList.add(obj);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public LinkedList<Object> getDataList() {
        return dataList;
    }

    public void setDataList(LinkedList<Object> dataList) {
        this.dataList = dataList;
    }
}
