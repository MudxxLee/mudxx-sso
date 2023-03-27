package com.mudxx.sso.common.oauth.filter;


import cn.hutool.json.JSONObject;
import com.mudxx.sso.oauth.http.OauthHttpServletRequest;
import com.mudxx.sso.oauth.util.RequestJsonUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author laiw
 * @description 实现/oauth/token请求参数重新封装的过滤器
 * @date 2023/3/23 16:20
 */
public class IntegrationAuthenticationFilter implements Filter {

    private static final String OAUTH_TOKEN_URL = "/oauth/token";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";

    private final RequestMatcher requestMatcher;

    public IntegrationAuthenticationFilter() {
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, HttpMethod.GET.name()),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, HttpMethod.POST.name())
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //匹配请求路径
        if (requestMatcher.matches(request)) {
            OauthHttpServletRequest oauthHttpServletRequest = new OauthHttpServletRequest(request);
            JSONObject json = RequestJsonUtil.getRequestJsonObject(oauthHttpServletRequest);
            Object clientId = json.get(CLIENT_ID);
            Object clientSecret = json.get(CLIENT_SECRET);
            Object grantType = json.get(GRANT_TYPE);
            oauthHttpServletRequest.setParameter(GRANT_TYPE, grantType);
            oauthHttpServletRequest.setParameter(CLIENT_ID, clientId);
            oauthHttpServletRequest.setParameter(CLIENT_SECRET, clientSecret);
            //参数改造
            String encoded = Base64.getEncoder().encodeToString((clientId + ":"
                    + clientSecret).getBytes(StandardCharsets.UTF_8));
            oauthHttpServletRequest.addHeader(AUTHORIZATION, BASIC + encoded);
            filterChain.doFilter(oauthHttpServletRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
