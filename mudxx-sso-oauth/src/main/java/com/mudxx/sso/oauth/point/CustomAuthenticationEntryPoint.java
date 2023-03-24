package com.mudxx.sso.oauth.point;


import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.common.web.util.WebUtils;
import com.mudxx.sso.oauth.exception.OauthResultCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Oauth2异常信息返回处理
 */
@Component
public class CustomAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if(e instanceof BadCredentialsException){
            // 如果是client_id和client_secret相关异常 返回自定义的数据格式
            CommonResult<?> commonResult = CommonResult.failed(OauthResultCode.INVALID_CLIENT);
            WebUtils.writeJsonToClient(response, commonResult);
        } else if(e instanceof InsufficientAuthenticationException) {
            // 如果是没有携带token
            CommonResult<?> commonResult = CommonResult.unauthorized();
            WebUtils.writeJsonToClient(response, commonResult);
        } else {
            super.commence(request,response,e);
        }
    }
}


