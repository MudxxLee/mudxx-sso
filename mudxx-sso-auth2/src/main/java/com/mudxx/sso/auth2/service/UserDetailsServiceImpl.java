package com.mudxx.sso.auth2.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.sso.auth2.entity.SSOUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此对象中实现远程服务调用
 * 并对用户信息进行封装返回,交给认证管理器(AuthenticationManager)
 * 去完成密码的比对操作.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 我们执行登录操作时,提交登录按钮系统会调用此方法
     * @param username 来自客户端用户提交的用户名
     * @return 封装了登录用户信息以及用户权限信息的一个对象,
     * 返回的UserDetails对象最终会交给认证管理器.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.基于用户名查找用户信息,判定用户是否存在
        SSOUser ssoUser = getDefaultByUsername(username);
        if(ObjectUtil.isEmpty(ssoUser)) {
            throw new UsernameNotFoundException("user is not exist");
        }
        //2.封装查询结果并返回
        return new User(username,
                ssoUser.getPassword(),
                AuthorityUtils.createAuthorityList(ssoUser.getPermissions().toArray(new String[]{})));
    }

    private SSOUser getDefaultByUsername(String username) {
        SSOUser ssoUser = new SSOUser();
        ssoUser.setUsername("admin");
        ssoUser.setPassword(passwordEncoder.encode("123456"));
        ssoUser.setPermissions(new ArrayList<>());
        return ssoUser;
    }

    private static final String URL_PREFIX = "http://127.0.0.1:9390/sso";

    private SSOUser getByUsername(String username) {
        SSOUser ssoUser = null;
        try {
            String url = URL_PREFIX + "/user/get/" + username;
            String result = HttpUtil.get(url, 5000);
            ssoUser = JSONUtil.toBean(result, SSOUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ObjectUtil.isNotEmpty(ssoUser)) {
            List<String> permissions = getUserPermissions(ssoUser.getId());
            ssoUser.setPermissions(permissions);
        }
        return ssoUser;
    }

    private List<String> getUserPermissions(Long id) {
        List<String> list = null;
        try {
            String url = URL_PREFIX + "/user/permission/" + id;
            String result = HttpUtil.get(url, 5000);
            list = JSONUtil.toList(result, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {

    }

}
