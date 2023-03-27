package com.mudxx.sso.oauth.resource;

import com.mudxx.sso.common.oauth.web.handler.CustomAccessDeniedHandler;
import com.mudxx.sso.common.oauth.web.point.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer //启动资源服务器的默认配置
@EnableGlobalMethodSecurity(prePostEnabled = true)//启动方法上的权限控制,需要授权才可访问的方法上添加@PreAuthorize等相关注解
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "res-oauth";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore).
                resourceId(RESOURCE_ID)
                .stateless(false);
        //自定义资源访问认证异常，没有token，或token错误，
        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        resources.accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 关闭跨域攻击
        http.csrf().disable();
        // 资源访问
        http.authorizeRequests()
                // 路径需认证
                .anyRequest().authenticated();
    }

}
