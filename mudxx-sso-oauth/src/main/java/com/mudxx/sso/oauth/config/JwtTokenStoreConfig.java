package com.mudxx.sso.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 构建令牌配置对象,在微服务架构中,登录成功后,可以将用户信息进行存储,常用存储方式
 * 有如下几种:
 * 1)产生一个随机字符串(token),然后基于此字符串将用户信息存储到关系数据库(例如mysql)
 * 2)产生一个随机字符串(token),然后基于此字符串将用户信息存储到内存数据库(例如redis)
 * 3)基于JWT创建令牌(Token),在此令牌中存储我们的用户信息,这个令牌不需要写到数据库,
 * 在客户端存储即可.
 * 基于如上设计方案,Oauth2协议中给出了具体的API实现对象,例如:
 * 1)JdbcTokenStore  (用的比较少了)
 * 2)RedisTokenStore (中型应用)
 * 3)JwtTokenStore (对性能要求比较高的分布式架构)
 */
@Configuration
public class JwtTokenStoreConfig {

    /**
     * 这里的签名key将来可以写到配置中心
     */
    private static final String SIGNING_KEY = "sso:auth";

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }

}
