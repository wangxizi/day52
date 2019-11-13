package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    //根据手机号判断是否是会员
    Member findByTelephone(String telephone);

    //自动注册为会员
    void add(Member dbMember);

    //根据年月查询会员数
    List<Integer> findMemberCountBeforeDate(List<String> moths);
}
