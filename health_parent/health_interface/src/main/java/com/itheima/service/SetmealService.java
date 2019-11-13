package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    //新增套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    //套餐分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    //查询所有套餐数据
    List<Setmeal> getSetmeal();

    //套餐详情页面数据展示
    Setmeal findById(Integer id);

    //套餐占比统计
    List<Map<String,Object>> findSetmealCount();

}
