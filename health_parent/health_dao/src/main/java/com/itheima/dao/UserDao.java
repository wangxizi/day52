package com.itheima.dao;

import com.itheima.pojo.User;

/*
* 用户服务持久层
* */
public interface UserDao {
    //根据用户名查询用户信息
    User findByUserName(String username);


}
