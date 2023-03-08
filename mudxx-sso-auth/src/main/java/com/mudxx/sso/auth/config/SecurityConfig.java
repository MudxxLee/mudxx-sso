package com.mudxx.sso.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 假如在前后端分离架构中,希望对登录成功和失败以后的信息以json
     * 形式进行返回,我们自己控制哪些url需要认证,哪些不需要认证,
     * 我们可以重写下面的方法进行自定义
     * 说明:这个方法是可以不写的.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //默认行为不需要了
        //super.configure(http);
        //1.关闭跨域攻击(这里的登录默认是post请求,但系统底层的跨域攻击设计是不允许post请求的,假如没有关闭跨域配置,后续登录时会抛出403异常)
        http.csrf().disable();
        //2.放行所有请求url(默认)
        //这种形式表示要认证
        //http.authorizeRequests().anyRequest().permitAll();//默认
        //3.配置登录成功和失败处理器(目的是响应到客户端的数据是json字符串)
        //默认没有配置,他会跳转到登录成功或失败的页面,而实际前后端分离架构
        //中服务端不负责页面跳转,服务端只负责返回json数据
        http.formLogin()//此方法执行后会生成一个/login的url
                //登录成功(success)处理器(handler)
                .successHandler(successHandler())
                .failureHandler(failureHandler());

    }

    /**
     * 定义认证成功处理器
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                Object principal = authentication.getPrincipal();
                System.out.println("principal="+principal);
                //1.定义响应数据
                Map<String,Object> map=new HashMap<>();
                map.put("status", 200);
                map.put("message", "login ok");
                //2.将响应数据写到客户端
                writeJsonToClient(response, map);
            }
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
                Map<String,Object> map=new HashMap<>();
                map.put("status", 500);
                map.put("message", "login error,"+e.getMessage());
                //2.将响应数据写到客户端
                writeJsonToClient(response, map);
            }
        };
    }

    private void writeJsonToClient(HttpServletResponse response,Map<String,Object> map) throws IOException {
        //2.1将map对象转换为json格式字符串
        String jsonResult=
                new ObjectMapper().writeValueAsString(map);
        //2.2设置响应数据编码和响应数据类型
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out= response.getWriter();
        out.write(jsonResult);
        out.flush();
    }
}
