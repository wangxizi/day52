package com.itheima.mobile.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/*
 * 体检预约-控制层
 * */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;


    /*
     * 体检预约
     * */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submit(@RequestBody Map map) {
        //验证码校验
        //1.获取用户名输入的验证码,手机号，体检日期
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String orderDate = (String) map.get("orderDate");
        //2.获取redis中的验证码
        String redisCode = jedisPool.getResource().get(telephone+RedisMessageConstant.SENDTYPE_ORDER);
        //3.验证码校验
        if (StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)) {
            //4.校验失败返回错误信息
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;//预约成功后页面需要的数据
        try {
            //5.校验通过后 调用service进行体检预约
            //6.预约类型设置
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submitOrder(map);//体检预约请求下单
            //6.预约成功后，发送预约成功通知短信
            if (result != null) {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return new Result(false, MessageConstant.ORDER_SUCCESS_FULL);
        }
        return result;
    }

    /*
     * 预约成功页面
     * */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id) {
        Map map = null;
        try {
            map = orderService.findById4Detail(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}
