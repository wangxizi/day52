package com.itheima.jobs;


import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    /*
     *清理垃圾图片任务类
     **/
    @Autowired
    private JedisPool jedisPool;

    //定时清理图片
    public void clearImg() {
        System.out.println("定时清理垃圾图片运行了。。。");
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (sdiff != null && sdiff.size() > 0){
            for (String fileName : sdiff) {
                //调用七牛云删除接口 删除图片
                QiniuUtils.deleteFileFromQiniu(fileName);
                //删除redis中的垃圾数据
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
                System.out.println("删除成功:"+fileName);
            }
        }
    }
}
