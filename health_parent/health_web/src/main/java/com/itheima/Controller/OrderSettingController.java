package com.itheima.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/*
 * 预约管理控制层
 * */
@RestController
@RequestMapping(value = "/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    //文件上传并解析文件内容保存到数据库
    @RequestMapping(value = "/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            //解析文件
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingList = new ArrayList<>();
            //判断list
            if (list != null && list.size() > 0) {

                for (String[] str : list) {
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(new Date(str[0]));//预约日期
                    orderSetting.setNumber(Integer.parseInt(str[1]));//预约数量
                    orderSettingList.add(orderSetting);
                }
            }
            //调用业务层的方法将数据保存到数据库
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    //接收页面 年月请求返回List<Map>（显示预约设置数据）
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.GET)
    public Result getOrderSettingByMonth(String date){
        List<Map> listMap = null;
        try {
            listMap = orderSettingService.getOrderSettingByMonth(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,listMap);
    }

    /*
    * 单个预约设置
    * */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
