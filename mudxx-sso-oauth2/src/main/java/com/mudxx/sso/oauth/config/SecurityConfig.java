package com.mudxx.sso.oauth.config;

import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.common.web.util.WebUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author laiw
 * @date 2023/3/8 13:37
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 构建密码加密对象,登录时,系统底层会基于此对象进行密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 此对象要为后续的Oauth2的配置提供服务
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

//    /**
//     * 假如在前后端分离架构中,希望对登录成功和失败以后的信息以json
//     * 形式进行返回,我们自己控制哪些url需要认证,哪些不需要认证,
//     * 我们可以重写下面的方法进行自定义
//     * 说明:这个方法是可以不写的.
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //1.关闭跨域攻击
//        http.csrf().disable();
//        //2.放行所有请求url(默认)
//        //这种形式表示要认证
//        //http.authorizeRequests().anyRequest().permitAll();//默认
//        //3.配置登录成功和失败处理器(目的是响应到客户端的数据是json字符串)
//        //默认没有配置,他会跳转到登录成功或失败的页面,而实际前后端分离架构
//        //中服务端不负责页面跳转,服务端只负责返回json数据
//        http.formLogin()//此方法执行后会生成一个/login的url
//                //登录成功(success)处理器(handler)
//                .successHandler(successHandler())
//                .failureHandler(failureHandler());
//
//    }

    /**
     * 假如在前后端分离架构中,希望对登录成功和失败以后的信息以json
     * 形式进行返回,我们自己控制哪些url需要认证,哪些不需要认证,
     * 我们可以重写下面的方法进行自定义
     * 说明:这个方法是可以不写的.
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //1.关闭跨域攻击
        httpSecurity.csrf().disable();
        //2.配置form认证
        httpSecurity.formLogin()
                //登录成功怎么处理?(向客户端返回json)
                .successHandler(successHandler())
                //登录失败怎么处理?(向客户端返回json)
                .failureHandler(failureHandler());
        //假如某个资源必须认证才可访问,那没有认证怎么办?(返回json)
//        httpSecurity.exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandler())
//                .authenticationEntryPoint(authenticationEntryPoint());
        //3.所有资源都要认证
        httpSecurity.authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/auth/token")
                .permitAll()
                .anyRequest()//所有请求->对应任意资源
                .authenticated();//必须认证
        // 基于token，所以不需要session
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
//        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        // 自行实现认证逻辑
        //auth.authenticationProvider();
    }

    /**
     * 定义认证成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return (request, response, authentication) -> {
            Object principal = authentication.getPrincipal();
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.success();
            //2.将响应数据写到客户端
            WebUtils.writeJsonToClient(response, result);
        };
    }

    /**
     * 失败处理器
     */
    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return (request, response, e) -> {
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.unauthorized();
            //2.将响应数据写到客户端
            WebUtils.writeJsonToClient(response, result);
        };
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
