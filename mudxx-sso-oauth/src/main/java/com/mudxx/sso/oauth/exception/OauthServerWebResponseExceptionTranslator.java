package com.mudxx.sso.oauth.exception;

import com.mudxx.sso.common.web.api.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;


/**
 * @author laiwen
 * @description 用户名, 密码错误，授权类型错误自定义异常解析器
 */
public class OauthServerWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<CommonResult<?>> translate(Exception e) {
        CommonResult<?> result = doTranslateHandler(e);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    private CommonResult<?> doTranslateHandler(Exception e) {
        //指定异常处理  根据业务实现
        if (e instanceof UnsupportedGrantTypeException) {
            return CommonResult.failed(OauthResultCode.UNSUPPORTED_GRANT_TYPE);
        } else if (e instanceof InvalidRequestException) {
            return CommonResult.failed(OauthResultCode.INVALID_REQUEST);
        } else if (e instanceof InvalidGrantException) {
            return CommonResult.failed(OauthResultCode.INVALID_GRANT);
        } else if(e instanceof InvalidTokenException) {
            return CommonResult.failed(OauthResultCode.INVALID_TOKEN);
        }
        return CommonResult.failed(OauthResultCode.UNAUTHORIZED_CLIENT);
    }
}
