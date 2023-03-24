package com.mudxx.sso.oauth.provider;

import com.mudxx.sso.oauth.exception.OauthResultCode;
import com.mudxx.sso.oauth.service.UserDetailsServiceImpl;
import com.mudxx.sso.oauth.service.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author laiw
 * @date 2023/3/24 11:30
 */

@Component
public class SecurityAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthenticationProvider.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 账号登录验证
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 这个获取表单输入中的用户名
        String username = authentication.getName();
        // 这个是表单中输入的密码
        String password = (String) authentication.getCredentials();
        UserVo userVo = (UserVo) userDetailsService.loadUserByUsername(username);
        if (userVo == null) {
            throw new BadCredentialsException(Long.toString(OauthResultCode.USERNAME_PASSWORD.getCode()));
        }
        if( ! passwordEncoder.matches(password, userVo.getPassword())) {
            throw new BadCredentialsException(Long.toString(OauthResultCode.USERNAME_PASSWORD.getCode()));
        }
        return new UsernamePasswordAuthenticationToken(userVo, password, userVo.getAuthorities());
    }

    /**
     * 执行支持判断
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}

