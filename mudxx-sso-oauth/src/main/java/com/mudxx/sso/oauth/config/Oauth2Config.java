package com.mudxx.sso.oauth.config;

import com.mudxx.sso.common.oauth.web.exception.OauthServerWebResponseExceptionTranslator;
import com.mudxx.sso.common.oauth.web.filter.CustomClientCredentialsTokenEndpointFilter;
import com.mudxx.sso.common.oauth.web.point.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Oauth2是一种协议或规范，定义了完成用户身份认证和授权的方式，
 * 比如：基于密码身份认证，基于指纹，基于第三方令牌认证（QQ，微信登录），
 * 但具体完成过程需要一组对象，这些对象构成，有如下几个部分：
 * 0.系统数据资源（类似数据库，文件系统）
 * 1.资源服务器（负责访问资源，例如：商品，订单，库存，会员...）
 * 2.认证服务器（负责完成用户身份的认证）
 * 3.客户端对象（表单，令牌，）
 * 4.资源拥有者（用户）
 *
 * 在Oauth2这中规范下，如何对用户进行认证？
 * 1.认证的地址（让用户去哪里认证）
 * 2.用户需要携带什么信息去认证（办理）
 * 3.具体完成认证的对象是谁
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private TokenEnhancer jwtTokenEnhancer;
    @Autowired
    private TokenEnhancer jwtAccessTokenConverter;

    /**
     * 配置认证规则
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 自定义异常翻译类
        endpoints.exceptionTranslator(new OauthServerWebResponseExceptionTranslator());
        // 配置由谁完成认证?(认证管理器)
        endpoints.authenticationManager(authenticationManager);
        // 配置由谁负责查询用户业务数据(可选)
        endpoints.userDetailsService(userDetailsService);
        // 配置token生成及存储策略(默认是UUID~随机的字符串)(存储到内存，mysql，redis，jwt)
        endpoints.tokenServices(tokenServices());
        // 配置可以处理的认证请求方式.(可选,默认只能处理post请求)
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
    }

    /**
     * 配置token生成及存储策略
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        //1.创建授权令牌服务对象(TokenServices)
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //2.配置令牌的创建和存储对象(TokenStore)
        tokenServices.setTokenStore(tokenStore);
        //3.配置令牌增强(默认令牌的生成非常简单,使用的就是UUID)
        TokenEnhancerChain tokenEnhancer = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        tokenEnhancer.setTokenEnhancers(delegates);
        tokenServices.setTokenEnhancer(tokenEnhancer);
        //4.设置令牌有效时长?   1小时
        tokenServices.setAccessTokenValiditySeconds(300);
        //5.设置是否支持刷新令牌刷(是否支持使用刷新令牌再生成新令牌)
        tokenServices.setSupportRefreshToken(true);
        //6.设置刷新令牌有效时长? 5小时
        tokenServices.setRefreshTokenValiditySeconds(600);
        //7.refresh_token是否重复使用
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    /**
     * 说明:我们的认证服务不是对任意客户端都要颁发令牌,是有条件的.
     * 通过此方法配置对谁颁发令牌?客户端需要有什么特点?
     * @param clients 定义客户端的配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //定义客户端要携带的id(客户端访问此服务时要携带的id,这个是自己定义的字符串)
                .withClient("dev")
                //定义客户端要携带的秘钥(这个秘钥也是官方定义的一个规则,客户端需要携带,字符串可以自己定义)
                .secret(passwordEncoder.encode("123456"))
                //定义作用范围(所有符合定义规则的客户端,例如client,secret,....)
                .scopes("all")
                // 定义可访问的资源
                .resourceIds("res-oauth", "res-demo")
                //定义允许的认证方式(可以基于密码进行认证,也可以基于刷新令牌进行认证)
                .authorizedGrantTypes("password", "refresh_token");
    }

    /**
     * 我们登录时要对哪个url发起请求,通过哪个url可以解析令牌等?
     * 配置要对外暴露的认证url,刷新令牌的url,检查令牌的url等
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //公开认证地址（/auth/token）
        security.tokenKeyAccess("permitAll()")
                //公开检查令牌的入口(/oauth/check_token)
                .checkTokenAccess("permitAll()");
//                //通过验证返回token信息(/oauth/check_token)
//                .checkTokenAccess("isAuthenticated()");

        //允许通过表单方式进行认证(
//        security.allowFormAuthenticationForClients();
        /**
         * 此处，在configure中，不再需要配置allowFormAuthenticationForClients()。
         * allowFormAuthenticationForClients的作用是让/oauth/token支持client_id以及client_secret作登录认证，
         * oauthServerSecurity.allowFormAuthenticationForClients();即设置allowFormAuthenticationForClients = true;作用是在BasicAuthenticationFilter之前添加ClientCredentialsTokenEndpointFilter，使用ClientDetailUserDeailsService来进行client端登录的验证。
         * 同时，使用allowFormAuthenticationForClients()和我们自定义的CustomClientCredentialsTokenEndpointFilter，会导致oauth2仍然使用allowFormAuthenticationForClients中默认的ClientCredentialsTokenEndpointFilter进行过滤，致使我们的自定义CustomClientCredentialsTokenEndpointFilter不生效。
         * 因此，在使用CustomClientCredentialsTokenEndpointFilter时，不再需要开启allowFormAuthenticationForClients()功能。
         */
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(new CustomAuthenticationEntryPoint());
        // 客户端认证之前的过滤器
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

    }
}
