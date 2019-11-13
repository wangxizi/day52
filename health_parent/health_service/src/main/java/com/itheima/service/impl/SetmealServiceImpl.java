package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 套餐业务层
* */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.往套餐表中插入数据
        setmealDao.add(setmeal);
        //2.往中间表插入数据 套餐表id 检查组ids
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
    }
    //套餐分页查询
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //基于Mybatis分页助手插件实现分页
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    //查询所有套餐数据
    @Override
    public List<Setmeal> getSetmeal() {

        return setmealDao.getSetmeal();
    }


    //套餐详情页面数据展示
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //套餐占比统计
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //设置套餐表和检查组中间表
    public void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkgroupId);
                map.put("setmealId",setmealId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
