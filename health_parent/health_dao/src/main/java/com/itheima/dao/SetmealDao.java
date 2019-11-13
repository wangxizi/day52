package com.itheima.dao;


import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/*
* 套餐持久层
* */
public interface SetmealDao {

    //新增套餐
    void add(Setmeal setmeal);


    //设置套餐表和检查组中间表
    void setSetmealAndCheckGroup(Map<String, Integer> map);


    //套餐分页查询
    Page<Setmeal> selectByCondition(String queryString);

    //查询所有套餐数据
    List<Setmeal> getSetmeal();

    //套餐详情页面数据展示(套餐 检查组 检查项数据)
    Setmeal findById(Integer id);

    //套餐占比统计
    List<Map<String,Object>> findSetmealCount();

    //热门套餐
    List<Map> hotSetmeal();

}
