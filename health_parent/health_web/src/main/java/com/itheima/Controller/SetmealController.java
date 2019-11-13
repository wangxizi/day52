package com.itheima.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;


import java.util.UUID;

/*
 * 套餐管理控制层
 * */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
       private JedisPool jedisPool;

    /*
    * 图片上传七牛云
    * 回显图片
    * */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(@RequestParam("imgFile")MultipartFile multipartFile){
        try {
            String originalFilename = multipartFile.getOriginalFilename();//原始文件名
            String suffx = originalFilename.substring(originalFilename.lastIndexOf("."));//后缀
            //使用UUID随机产生一个不重复的文件名
            String newFileName = UUID.randomUUID().toString()+suffx;//获取了一个新的名字
            //上传文件
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),newFileName);
            //将上传图片的记录放到redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            //回显图片 将图片名称 设置到result对象中
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }


    //新增套餐
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('SETMEAL_ADD')")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            //新增成功后将记录上传到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //套餐分页查询
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }
}
