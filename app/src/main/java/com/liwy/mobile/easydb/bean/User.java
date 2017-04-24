package com.liwy.mobile.easydb.bean;

import com.liwy.mobile.easydb.annotation.Column;
import com.liwy.mobile.easydb.annotation.Id;
import com.liwy.mobile.easydb.annotation.Table;

/**
 * Created by liwy on 2017/4/18.
 */
@Table("user")
public class User {
    @Id(value = "_id")
    public int id;
    @Column(value = "name")
    private String name;
    private int age;

    public User() {
    }

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "idInfo=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
