package com.mudxx.sso.oauth.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.sso.oauth.service.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        UserVo userVo = getDefaultByUsername(username);
        if(ObjectUtil.isEmpty(userVo)) {
            // 异常交由后续统一处理
            return null;
        }
        return userVo;
    }

    private UserVo getDefaultByUsername(String username) {
        UserVo userVo = new UserVo();
        userVo.setUsername(username);
        userVo.setPassword("$2a$10$oX1bYtXp.eIwYlnFZbfX8uvEis0a6fNb47Y0aVQfHkmLfhno7bsKK");
        userVo.setPermissions(new ArrayList<>());
        userVo.setEnabled(true);
        return userVo;
    }

    public static final String URL_PREFIX = "http://127.0.0.1:9390/sso";

    private UserVo getByUsername(String username) {
        UserVo userVo = null;
        try {
            String url = URL_PREFIX + "/user/get/" + username;
            String result = HttpUtil.get(url, 5000);
            userVo = JSONUtil.toBean(result, UserVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userVo;
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
