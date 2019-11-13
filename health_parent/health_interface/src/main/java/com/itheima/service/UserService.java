package com.itheima.service;


import com.itheima.pojo.User;

/*
* 用户接口服务
* */
public interface UserService {

    //根据用户名查询用户信息
    User findByUserName(String username);



}
