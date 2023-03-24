package com.mudxx.sso.oauth.exception;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mudxx.sso.common.web.api.CommonResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * Oauth异常处理器
 * @author laiw
 * @date 2023/3/24 09:49
 */
public class OauthExceptionProcessor {

    public static CommonResult<?> processor(OAuth2Exception e) {
        if (ObjectUtil.isEmpty(e) || StrUtil.isBlank(e.getOAuth2ErrorCode())) {
            return CommonResult.failed(OauthResultCode.UNAUTHORIZED, StrUtil.blankToDefault(e.getMessage(), OauthResultCode.UNAUTHORIZED.getMessage()));
        }
        String oAuth2ErrorCode = e.getOAuth2ErrorCode();
        switch (oAuth2ErrorCode) {
            case OAuth2Exception.INVALID_REQUEST:
                return CommonResult.failed(OauthResultCode.INVALID_REQUEST);
            case OAuth2Exception.INVALID_CLIENT:
                return CommonResult.failed(OauthResultCode.INVALID_CLIENT);
            case OAuth2Exception.INVALID_GRANT:
                if(StrUtil.equals(Long.toString(OauthResultCode.USERNAME_PASSWORD.getCode()), e.getMessage())) {
                    return CommonResult.failed(OauthResultCode.USERNAME_PASSWORD);
                }
                if(StrUtil.equals(Long.toString(OauthResultCode.REDIRECT_URI_MISMATCH.getCode()), e.getMessage())) {
                    return CommonResult.failed(OauthResultCode.REDIRECT_URI_MISMATCH);
                }
                return CommonResult.failed(OauthResultCode.INVALID_GRANT);
            case OAuth2Exception.UNAUTHORIZED_CLIENT:
                return CommonResult.failed(OauthResultCode.UNAUTHORIZED_CLIENT);
            case OAuth2Exception.UNSUPPORTED_GRANT_TYPE:
                return CommonResult.failed(OauthResultCode.UNSUPPORTED_GRANT_TYPE);
            case OAuth2Exception.INVALID_SCOPE:
                return CommonResult.failed(OauthResultCode.INVALID_SCOPE);
            case OAuth2Exception.INVALID_TOKEN:
                return CommonResult.failed(OauthResultCode.INVALID_TOKEN);
            case OAuth2Exception.UNSUPPORTED_RESPONSE_TYPE:
                return CommonResult.failed(OauthResultCode.UNSUPPORTED_RESPONSE_TYPE);
            case OAuth2Exception.REDIRECT_URI_MISMATCH:
                return CommonResult.failed(OauthResultCode.REDIRECT_URI_MISMATCH);
            default:
                return CommonResult.failed(OauthResultCode.UNAUTHORIZED, StrUtil.blankToDefault(e.getMessage(), OauthResultCode.UNAUTHORIZED.getMessage()));
        }
    }

}
