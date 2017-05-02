package com.easydb.mobile.easydb.bean;


import com.easydb.annotation.Exclusion;
import com.easydb.annotation.Id;
import com.easydb.annotation.OneToMany;
import com.easydb.annotation.Table;

import java.util.List;

/**
 * Created by liwy on 2017/4/28.
 */

@Table(value = "tb_house")
public class House {
    @Id
    private int id;

    private String houseName;

    @OneToMany(associatedColumn = "houseId")
    private List<User> users;

    public House() {
    }

    public House(int id, String houseName) {
        this.id = id;
        this.houseName = houseName;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", houseName='" + houseName + '\'' +
                ", users=" + (users != null ? users.size() : 0) +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
