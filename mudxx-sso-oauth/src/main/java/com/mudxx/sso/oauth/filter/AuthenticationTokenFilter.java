package com.mudxx.sso.oauth.filter;

import cn.hutool.core.util.StrUtil;
import com.mudxx.component.redis.utils.StringRedisUtils;
import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.common.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private StringRedisUtils stringRedisUtils;
    @Autowired
    private TokenStore tokenStore;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if( ! request.getRequestURI().startsWith("/api")) {
            chain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader(this.tokenHeader);
        if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith(this.tokenHead)) {
            String accessToken = authHeader.substring(this.tokenHead.length());
            logger.info("自定义拦截: {}", accessToken);
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
            if(oAuth2AccessToken.isExpired()) {
                CommonResult<Object> result = CommonResult.unauthorized();
                WebUtils.writeJsonToClient(response, result);
                return;
            }

            final String redisKey = "oauth:token:username:" + oAuth2AccessToken.getAdditionalInformation().get("username");
            final String redisValue = (String) oAuth2AccessToken.getAdditionalInformation().get("jti");
            if( ! stringRedisUtils.hasKey(redisKey)) {
                CommonResult<Object> result = CommonResult.unauthorized();
                WebUtils.writeJsonToClient(response, result);
                return;
            }
            if( ! redisValue.equals(stringRedisUtils.get(redisKey))) {
                CommonResult<Object> result = CommonResult.unauthorized();
                WebUtils.writeJsonToClient(response, result);
                return;
            }

        }
        chain.doFilter(request, response);
    }
}
