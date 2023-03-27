package com.mudxx.sso.common.oauth.web.point;


import com.mudxx.sso.common.oauth.web.exception.OauthExceptionProcessor;
import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.common.web.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Oauth2异常信息返回处理
 * @author laiwen
 */
public class CustomAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        CommonResult<?> commonResult = OauthExceptionProcessor.processorAuthenticationException(e);
        WebUtils.writeJsonToClient(response, commonResult);
    }
}


