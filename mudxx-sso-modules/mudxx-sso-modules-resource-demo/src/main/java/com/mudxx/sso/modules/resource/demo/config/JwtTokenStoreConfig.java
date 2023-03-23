package com.mudxx.sso.modules.resource.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author laiwen
 */
@Configuration
public class JwtTokenStoreConfig {
    /**
     * 解密口令
     */
    private static final String SIGNING_KEY = "sso:auth";

    /**
     * 为AuthorizationServer提供JWT令牌的加密对象
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * Jwt转换器,将任何数据转换为jwt字符串
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
        //设置加密/解密口令
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }
}
