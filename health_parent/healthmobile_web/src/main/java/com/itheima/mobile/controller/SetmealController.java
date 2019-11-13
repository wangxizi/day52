package com.itheima.mobile.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
*
*移动端表现控制层
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;
    //查询出所有套餐数据

    @RequestMapping(value = "/getSetmeal",method = RequestMethod.POST)
    public Result getSetmeal(){
        try {
            List<Setmeal> list = setmealService.getSetmeal();
           return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    //套餐详情页面数据展示(套餐 检查组 检查项数据)
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }

}
