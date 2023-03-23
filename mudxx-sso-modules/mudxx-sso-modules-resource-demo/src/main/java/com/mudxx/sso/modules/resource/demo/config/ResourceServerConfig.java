package com.mudxx.sso.modules.resource.demo.config;

import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.common.web.util.WebUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    /**
     * 路由安全认证配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //1.关闭跨域攻击
        http.csrf().disable();
        //2.设置拒绝处理器(不允许访问资源时,应该给出什么反馈)
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());
        //3.资源访问(所有资源在本项目中的访问不进行认证)
        http.authorizeRequests().anyRequest().authenticated();
    }

    /**假如没有登录访问资源时给出提示*/
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request,response,exception)->{
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.forbidden();
            //2.将响应数据写到客户端
            WebUtils.writeJsonToClient(response, result);
        };
    }

    /**假如没有登录访问资源时给出提示*/
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request,response,exception)->{
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.unauthorized();
            //2.将响应数据写到客户端
            WebUtils.writeJsonToClient(response, result);
        };
    }
}
