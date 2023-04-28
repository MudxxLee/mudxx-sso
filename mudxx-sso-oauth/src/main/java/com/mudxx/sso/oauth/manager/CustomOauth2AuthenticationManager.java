package com.mudxx.sso.oauth.manager;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

import java.util.UUID;

/**
 * 认证管理器
 * 继承于原本的OAuth2认证管理器,替代原本的认证管理器
 * 目的是做到如果原来的认证管理器如果提取token并认证失败报错的话,提供一个匿名的访问认证 {@link AnonymousAuthenticationToken}
 * 最终效果是实现即时请求 无须认证 的接口时带着 过期 或 无效 的 token 时,可以正常继续请求而不会因为 过期或无效的token 导致请求失败
 * @author laiw
 * @date 2023/4/26 16:46
 */
public class CustomOauth2AuthenticationManager extends OAuth2AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            return super.authenticate(authentication);
        } catch (AuthenticationException | InvalidTokenException e) {
            return new AnonymousAuthenticationToken(
                    UUID.randomUUID().toString(),
                    "anonymousUser",
                    AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        }
    }

}
