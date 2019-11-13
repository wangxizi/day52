package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/*
* 检查项业务层
* */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;


    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
            checkItemDao.add(checkItem);

    }

    //检查项分页
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //基于Mybatis分页助手插件实现分页
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
       return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }
    //删除检查项
    @Override
    public void deleteByCheckItemId(int id) {
        //1.根据检查项id 到中间表中查询 t_checkgroup_checkitem
        int count = checkItemDao.findCountByCheckItemId(id);

        if (count>0){
            //2.存在 跟检查组关系
            //3.提示用户不能删除此检查项
            throw new RuntimeException("此检查项已经关联检查组，不能删除");
        }else {
            //4.不存在检查组关系 直接删除检查项记录
            checkItemDao.deleteByCheckItemId(id);
        }
    }
    //根据id查询检查项
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    //编辑检查项
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
    //查询所有检查项数据
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }



}
