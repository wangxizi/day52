package com.itheima.Controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;




/*
* 用户管理表现层
* */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 从springSecurity框架容器中获取用户信息
     *
     * 注意：
     *    容器中的用户信息怎么来的？
     *    认证后springSecurity框架将认证成功后的用户信息放到容器中。
     */
    //获取当前用户登录的用户名
    @RequestMapping(value = "/findUserName",method = RequestMethod.GET)
    public Result findUserName(){
        //SecurityContext:容器对象
        SecurityContext context = SecurityContextHolder.getContext();
        //authentication:认证对象（登录后的信息）
        Authentication authentication = context.getAuthentication();
        //String username = authentication.getName();//获取用户登录的用户名
        User principal = (User) authentication.getPrincipal();//获取User对象
        String username = principal.getUsername();//获取用户名登录的用户名
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }
}
