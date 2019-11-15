package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;import java.util.HashMap;
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

    @Autowired
    private JedisPool jedisPool;

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

    //查询所有套餐信息
    @Override
    public List<Setmeal> getSetmeal() {
        List<Setmeal> setmeals = null;
        try {
            //1.首先从redis中获取
            String allSetmeal = jedisPool.getResource().get(RedisConstant.ALL_SETMEAL);
            ObjectMapper objectMapper = new ObjectMapper();
            if (allSetmeal != null) {
                //1.2 将从redis中获取到的json字符串转换成java对象
                setmeals = objectMapper.readValue(allSetmeal, new TypeReference<List<Setmeal>>(){});
                return setmeals;
            }
            //2.1reids中没有，则从mysql中获取
            setmeals = setmealDao.getSetmeal();
            //2.2 从mysql中获取了后转换成json格式字符创  然后存入redis中
            //将套餐集合转换为json
            String jsonSetmeals = objectMapper.writeValueAsString(setmeals);
            //将json存入redis
            String set = jedisPool.getResource().set(RedisConstant.ALL_SETMEAL, jsonSetmeals);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setmeals;
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
