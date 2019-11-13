package com.itheima.dao;


import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
* 预约管理持久层
* */
public interface OrderSettingDao {

    //根据预约日期查询是否进行了预约设置
    int findCountByOrderDate(Date orderDate);

    //如果已经预约设置了 更新预约设置
    //根据预约日期修改预约人数
    void editNumberByOrderDate(OrderSetting orderSetting);

    //如果没有预约设置 直接持久化数据（保存）
    void add(OrderSetting orderSetting);

    //根据起始日期和 结束日期 查询预约数据
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    //根据预约日期查询预约设置记录
    OrderSetting findByOrderDate(Date newOrderDate);
}
