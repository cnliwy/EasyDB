package com.liwy.mobile.easydb.bean;

import com.liwy.mobile.easydb.annotation.Id;
import com.liwy.mobile.easydb.annotation.OneToMany;
import com.liwy.mobile.easydb.annotation.Table;

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
}
