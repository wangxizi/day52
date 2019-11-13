package com.itheima.dao;


import com.itheima.pojo.Role;

import java.util.Set;

/*
* 角色服务持久层
* */
public interface RoleDao {

    //根据用户id查询角色信息
    Set<Role> findRoleByUserId(Integer id);
}
