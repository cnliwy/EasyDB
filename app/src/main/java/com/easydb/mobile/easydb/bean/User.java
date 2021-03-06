package com.easydb.mobile.easydb.bean;


import com.easydb.annotation.Column;
import com.easydb.annotation.Id;
import com.easydb.annotation.ManyToOne;
import com.easydb.annotation.Table;

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
    private String remark;
    private String date;
    private double weight;
    private boolean isDead;
    private boolean married;//是否已婚

    @ManyToOne(associatedColumn = "houseId" )
    private House house;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
