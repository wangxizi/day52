package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Menu;
import com.itheima.service.MemberService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/*
 *
 *用户使用手机登录表现层
 * */
@RestController
@RequestMapping("/login")
public class IoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Result check(@RequestBody Map map, HttpServletResponse response) {
        String telephone = (String) map.get("telephone");//手机号
        String validateCode = (String) map.get("validateCode");//用户输入的验证码
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(validateCode)) {
            return new Result(false, MessageConstant.PARAM_FAIL);
        }
        //取出redis里面的验证码
        String redisCode = jedisPool.getResource().get(telephone+RedisMessageConstant.SENDTYPE_LOGIN);
        //将两个验证码进行校验
        if (StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)) {
            //校验失败返回错误信息
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //走到这里说明校验通过
        //判断是否是会员
        //根据手机号判断是否是会员
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            //不是会员，自动注册为会员
            //自动注册后也要 将用户信息写入cookie
            Member dbMember = new Member();
            dbMember.setPhoneNumber(telephone);
            dbMember.setRegTime(new Date());
            memberService.add(dbMember);
        }
        //是会员，通过cookie将用户信息写入
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/");//在所有路径下都可以使用cookie
        cookie.setMaxAge(30 * 60 * 60 * 24);
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

}
