package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/*
* 权限服务持久层
* */
public interface PermissionDao {

    //根据角色id查询权限信息
    Set<Permission> findPermissionByRoleId(Integer id);
}
