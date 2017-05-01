package com.easydb.mobile.easydb.bean;


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
}
