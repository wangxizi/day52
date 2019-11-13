package com.itheima.dao;


/*
* 检查项持久层
* */
import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    //新增检查项
     void add(CheckItem checkItem);

    //检查项分页
    Page<CheckItem> selectByCondition(String queryString);

    //根据id查询是否关联检查组
    int findCountByCheckItemId(int id);

    //删除检查项
    void deleteByCheckItemId(int id);

    //根据id查询检查项
    CheckItem findById(int id);

    //编辑检查项
    void edit(CheckItem checkItem);
    //查询所有检查项数据
    List<CheckItem> findAll();

}
