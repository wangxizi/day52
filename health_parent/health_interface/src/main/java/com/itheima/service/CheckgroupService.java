package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckgroupService {
    //新增检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    //检查组分页
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    //根据检查组id查询检查组数据
    CheckGroup findById(int id);
    //根据检查组id查询检查项ids
    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);
    //编辑检查组
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);
    //查询检查组信息
    List<CheckGroup> findAll();
    //删除检查组
    void delete(int id);

}
