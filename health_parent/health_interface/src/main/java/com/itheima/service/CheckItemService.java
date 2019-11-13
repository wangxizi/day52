package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    //新增检查项
    void add(CheckItem checkItem);
    //检查项分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
    //删除检查项
    void deleteByCheckItemId(int id);
    //根据id查询检查项
    CheckItem findById(int id);
    //编辑检查项
    void edit(CheckItem checkItem);
    //查询所有检查项数据
    List<CheckItem> findAll();
}
