package com.mudxx.sso.auth2.config;

import com.mudxx.sso.auth2.component.JwtAuthenticationTokenFilter;
import com.mudxx.sso.common.web.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author laiw
 * @date 2023/3/8 13:37
 */
@Configuration
@EnableWebSecurity
//启动方法上的权限控制,需要授权才可访问的方法上添加@PreAuthorize等相关注解
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

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
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(deniedHandler())
                .authenticationEntryPoint(entryPoint());
        //3.所有资源都要认证
        httpSecurity.authorizeRequests()
                .antMatchers("/api/auth/**")
                .permitAll()
                .anyRequest()//所有请求->对应任意资源
                .authenticated();//必须认证
        // 基于token，所以不需要session
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * 定义认证成功处理器
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return (request, response, authentication) -> {
            Object principal = authentication.getPrincipal();
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.success();
            //2.将响应数据写到客户端
            writeJsonToClient(response, result);
        };
    }

    /**
     * 失败处理器
     * @return
     */
    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                //1.定义响应数据
                CommonResult<Object> result = CommonResult.unauthorized();
                //2.将响应数据写到客户端
                writeJsonToClient(response, result);
            }
        };
    }

    /**假如没有登录访问资源时给出提示*/
    private AccessDeniedHandler deniedHandler(){
        return (request,response,exception)->{
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.forbidden();
            //2.将响应数据写到客户端
            writeJsonToClient(response, result);
        };
    }

    /**假如没有登录访问资源时给出提示*/
    private AuthenticationEntryPoint entryPoint(){
        return (request,response,exception)->{
            //1.定义响应数据
            CommonResult<Object> result = CommonResult.unauthorized();
            //2.将响应数据写到客户端
            writeJsonToClient(response, result);
        };
    }


    private void writeJsonToClient(HttpServletResponse response, CommonResult<Object> result) throws IOException {
        //设置响应数据编码和响应数据类型
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out= response.getWriter();
        out.write(result.toString());
        out.flush();
    }
}
