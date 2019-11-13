package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sun.security.rsa.RSASignature;

import java.util.ArrayList;
import java.util.List;


/*
 *会员管理业务层
 *
 * */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;


    //根据手机号判断是否是会员
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //新增一条会员
    @Override
    public void add(Member dbMember) {
        memberDao.add(dbMember);
    }

    //根据年月查询会员数
    @Override
    public List<Integer> findMemberCountBeforeDate(List<String> moths) {
        List<Integer> list = new ArrayList<>();
        for (String moth : moths) {
            moth = moth + "-31";//拼接成2019-11-31格式
            Integer integer =memberDao.findMemberCountBeforeDate(moth);
            list.add(integer);
        }
        return list;
    }
}
