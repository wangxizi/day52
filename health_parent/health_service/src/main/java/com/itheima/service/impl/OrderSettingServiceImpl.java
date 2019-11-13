package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 预约管理业务层
* */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    //文件上传并解析文件内容保存到数据库
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        //将批量预约数据持久化数据库中
        //遍历for 调用dao方法保存数据库
        if (orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //1.根据预约日期查询是否进行了预约设置
               int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
               if (count>0){
                   //2.如果已经预约设置了 更新预约设置
                   //根据预约日期修改预约人数
                    orderSettingDao.editNumberByOrderDate(orderSetting);
               }else {
                   //3.如果没有预约设置 直接持久化数据（保存）
                   orderSettingDao.add(orderSetting);
               }
            }
        }


    }

    /*
    * 显示预约设置数据
    * */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //date=2019-11
        String startDate = date+"-01";
        String endDate = date+"-31";
        Map<String,String> map = new HashMap<>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        //根据起始日期和结束日期查询预约数据集合
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);
        //将返回的结果集 转成 List<map>返回
        List<Map> rsListMap = new ArrayList<>();
        if (orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String,Object> rsMap = new HashMap<>();
                rsMap.put("date",orderSetting.getOrderDate().getDate());//几号
                rsMap.put("number",orderSetting.getNumber());//可预约人数
                rsMap.put("reservations",orderSetting.getReservations());//已经预约人数
                rsListMap.add(rsMap);
            }
        }
        return rsListMap;
    }

    //单个预约设置
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //1根据预约日期查询 是否已经预约
        int countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (countByOrderDate>0){
            //如果以及预约则修改
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //如果没预约则增加
            orderSettingDao.add(orderSetting);
        }

    }
}
