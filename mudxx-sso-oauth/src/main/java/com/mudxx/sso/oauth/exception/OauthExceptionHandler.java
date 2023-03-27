package com.mudxx.sso.oauth.exception;

import com.mudxx.sso.common.oauth.web.exception.OauthExceptionProcessor;
import com.mudxx.sso.common.web.api.CommonResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 * @author laiwen
 */
@ControllerAdvice
public class OauthExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public CommonResult<?> handleOauth2(OAuth2Exception e) {
        System.out.println(e.getSummary());
        System.out.println(e.getOAuth2ErrorCode());
        System.out.println(e.getMessage());
        return OauthExceptionProcessor.processorOAuth2Exception(e);
    }
}
