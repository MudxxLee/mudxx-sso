package com.mudxx.sso.system.controller;

import com.mudxx.sso.system.pojo.SSOUser;
import com.mudxx.sso.system.service.SSOUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sso/user")
public class UserController {

    @Autowired
    private SSOUserService ssoUserService;
    /**
     * 基于用户名查询用户信息,后续在sso-auth服务中会对这个方法进行远程调用
     * @param username
     * @return
     */
    @GetMapping("/get/{username}")
    public SSOUser doSelectUserByUsername(@PathVariable("username") String username){
        return ssoUserService.selectUserByUsername(username);
    }
    /**
     * 基于用户id查询用户权限,后续会在sso-auth工程中对此方法进行远程调用.
     * @param userId
     * @return
     */
    @GetMapping("/permission/{userId}")
    public List<String> doSelectUserPermissions(
            @PathVariable("userId") Long userId){
        return ssoUserService.selectUserPermissions(userId);
    }

}
