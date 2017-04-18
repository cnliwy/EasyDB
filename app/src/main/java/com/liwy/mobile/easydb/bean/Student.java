package com.liwy.mobile.easydb.bean;

import com.liwy.mobile.easydb.annotation.Column;
import com.liwy.mobile.easydb.annotation.Table;

/**
 * Created by liwy on 2017/4/18.
 */
@Table("tb_student")
public class Student {
    @Column(require = true)
    private int _id;

    @Column(value = "name")
    private String sduName;

    private String sex;

    public Student(int _id, String name, String sex) {
        this._id = _id;
        this.sduName = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + sduName + '\'' +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return sduName;
    }

    public void setName(String name) {
        this.sduName = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
