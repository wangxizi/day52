package com.itheima.dao;



import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;
import java.util.Map;

/*
* 检查组持久层
* */

public interface CheckgroupDao {

    //新增检查组
    void add(CheckGroup checkGroup);

    //设置检查组和检查项的中间表
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    //检查组分页
    Page<CheckGroup> selectByCondition(String queryString);

    //根据检查组id查询检查组数据
    CheckGroup findById(int id);

    //根据检查组id查询检查项ids
    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);

    //更新检查组数据
    void edit(CheckGroup checkGroup);

    //根据检查组id清空检查项数据
    void deleteRelationByCheckGroupId(Integer groupId);

    //查询检查组信息
    List<CheckGroup> findAll();

    //根据id删除中间表
    void deleteStaging(int id);

    //根据id删除检查组
    void deleteInspection(int id);
}
