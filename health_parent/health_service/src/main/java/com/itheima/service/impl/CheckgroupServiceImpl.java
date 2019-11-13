package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
* 检查组业务层
* */
@Service(interfaceClass =CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupDao checkgroupDao;


    //新增检查组
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1.往检查组表中插入数据
        checkgroupDao.add(checkGroup);
        //2.往中间表插入数据 检查组id 检查项ids
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);


    }

    //检查组分页
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //基于Mybatis分页助手插件实现分页
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> checkItemPage = checkgroupDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }

    //根据检查组id查询检查组数据
    @Override
    public CheckGroup findById(int id) {

        return checkgroupDao.findById(id);
    }

    //根据检查组id查询检查项ids
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId) {

        return checkgroupDao.findCheckItemIdsByCheckGroupId(checkGroupId);
    }

    //编辑检查组
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //检查组id
        Integer groupId = checkGroup.getId();
        //更新检查组数据
        checkgroupDao.edit(checkGroup);
        //根据检查组id清空检查项数据
        checkgroupDao.deleteRelationByCheckGroupId(groupId);
        setCheckGroupAndCheckItem(groupId,checkitemIds);
    }



    //设置检查组和检查项中间表
    public void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkgroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }



    //查询检查组信息
    @Override
    public List<CheckGroup> findAll() {
        return checkgroupDao.findAll();
    }

    //删除检查组
    @Override
    public void delete(int id) {
        //根据id删除中间表
        checkgroupDao.deleteStaging(id);
        //根据id删除检查组
        checkgroupDao.deleteInspection(id);
    }


}
