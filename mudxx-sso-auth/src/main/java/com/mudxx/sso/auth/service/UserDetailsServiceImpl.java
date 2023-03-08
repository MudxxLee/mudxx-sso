package com.mudxx.sso.auth.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.sso.auth.entity.SSOUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 在此对象中实现远程服务调用
 * 并对用户信息进行封装返回,交给认证管理器(AuthenticationManager)
 * 去完成密码的比对操作.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
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
        SSOUser ssoUser = getByUsername(username);
        if(ssoUser==null) {
            throw new UsernameNotFoundException("user is not exist");
        }
        //2.基于用户id查询用户权限(登录用户不一定可以访问所有资源)
        final List<String> permissions = getUserPermissions(ssoUser.getId());
        //sys:res:create,sys:res:delete
        log.debug("permissions: {}",permissions.toString());
        //3.封装查询结果并返回.
        return new User(username, ssoUser.getPassword(), AuthorityUtils.createAuthorityList(permissions.toArray(new String[]{})));
    }

    public static final String URL_PREFIX = "http://127.0.0.1:9390/sso";

    private SSOUser getByUsername(String username) {
        SSOUser ssoUser = null;
        try {
            String url = URL_PREFIX + "/user/get/" + username;
            String result = HttpUtil.get(url, 5000);
            ssoUser = JSONUtil.toBean(result, SSOUser.class);
        } catch (Exception e) {
            e.printStackTrace();
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
