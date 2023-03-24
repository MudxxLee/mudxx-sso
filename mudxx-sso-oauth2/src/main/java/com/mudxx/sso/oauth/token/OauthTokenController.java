package com.mudxx.sso.oauth.token;

import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.oauth.exception.OauthServerWebResponseExceptionTranslator;
import com.mudxx.sso.oauth.token.dto.OauthTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laiw
 * @description 自定义token请求和统一返回
 * @date 2023/3/23 15:38
 */
@RestController
@RequestMapping("/oauth")
public class OauthTokenController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * 重写/oauth/token这个默认接口，返回的数据格式统一
     */
    @PostMapping(value = "/token")
    @ResponseBody
    public CommonResult<?> postAccessToken(@RequestBody OauthTokenDto dto) throws HttpRequestMethodNotSupportedException {
        Map<String, String> params = new HashMap<>();
        params.put("username", dto.getUsername());
        params.put("password", dto.getPassword());
        params.put("grant_type", dto.getGrantType());
        params.put("client_secret", dto.getClientSecret());
        params.put("client_id", dto.getClientId());
        User clientUser = new User(dto.getUsername(), dto.getPassword(), new ArrayList<>());
        //生成已经认证的client
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword(), new ArrayList<>());
        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(token, params).getBody();
        return CommonResult.success(accessToken);
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<CommonResult<?>> handleException(OAuth2Exception e) {
        return new OauthServerWebResponseExceptionTranslator().translate(e);
    }

}

