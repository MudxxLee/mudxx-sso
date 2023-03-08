package com.mudxx.sso.system.service.impl;

import com.mudxx.sso.system.mapper.SSOUserMapper;
import com.mudxx.sso.system.pojo.SSOUser;
import com.mudxx.sso.system.service.SSOUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements SSOUserService {

    @Autowired
    private SSOUserMapper ssoUserMapper;

    @Override
    public SSOUser selectUserByUsername(String username) {
        return ssoUserMapper.selectUserByUsername(username);
    }

    @Override
    public List<String> selectUserPermissions(Long userId) {
        //方案1:在这里可以调用数据层的单表查询方法,查询三次获取用户信息
        //方案2:在这里可以调用数据层的多表嵌套或多表关联方法执行1次查询
        return ssoUserMapper.selectUserPermissions(userId);
    }
}
