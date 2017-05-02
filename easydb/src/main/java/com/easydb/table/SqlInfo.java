package com.easydb.table;

import java.util.LinkedList;

/**
 * sql信息类
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

    /**
     * 获取查询语句的参数值
     * @return
     */
    public String[] getArgsStringArray()
    {
        if (this.dataList != null)
        {
            String[] strings = new String[this.dataList.size()];
            for (int i = 0; i < this.dataList.size(); i++) {
                strings[i] = this.dataList.get(i).toString();
            }
            return strings;
        }
        return null;
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
