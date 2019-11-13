package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //文件上传并解析文件内容保存到数据库
    void add(List<OrderSetting> orderSettingList);

    //显示预约设置数据
    List<Map> getOrderSettingByMonth(String date);


    //单个预约设置
    void editNumberByDate(OrderSetting orderSetting);


}
