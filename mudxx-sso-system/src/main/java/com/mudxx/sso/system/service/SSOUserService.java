package com.mudxx.sso.system.service;

import com.mudxx.sso.system.pojo.SSOUser;

import java.util.List;

public interface SSOUserService {
    /**
     * 基于用户名查询用户信息
     * @param username
     * @return
     */
    SSOUser selectUserByUsername(String username);

    /**
     * 基于用户id查询用户权限
     * @param userId
     * @return
     */
    List<String> selectUserPermissions(Long userId);
}
