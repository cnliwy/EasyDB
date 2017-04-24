package com.liwy.mobile.easydb.table;

import java.util.LinkedList;

/**
 * Created by admin on 2017/4/20.
 */

public class SqlInfo {
    private String sql;
    private LinkedList<Object> dataList;

    /**
     * 添加数据
     * @param obj
     */
    public void addValue(Object obj)
    {
        this.dataList.add(obj);
    }

    /**
     * 获取数据数组
     * @return
     */
    public Object[] getValues(){
        return dataList != null ? dataList.toArray(): null;
    }

    public SqlInfo() {
        dataList = new LinkedList<Object>();
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
