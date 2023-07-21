package com.mudxx.sso.oauth.web.token;

import cn.hutool.json.JSONUtil;
import com.mudxx.component.redis.utils.StringRedisUtils;
import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.oauth.web.token.dto.Oauth2TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
@RequestMapping("/oauth")
public class AuthTokenController {

    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private StringRedisUtils stringRedisUtils;
    @Autowired
    private TokenStore tokenStore;

    /**
     * Oauth2登录认证
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<Oauth2TokenDto> postAccessToken(Principal principal,
                                                        @RequestParam Map<String, String> parameters,
                                                        @RequestHeader(value = "terminal", required = true) String terminal)
            throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        System.out.println(JSONUtil.toJsonStr(oAuth2AccessToken));
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();


        Set<String> test = stringRedisUtils.getForSet("test");


        final String redisKey = "oauth:token:username:" + oAuth2AccessToken.getAdditionalInformation().get("username");
        final String redisValue = (String) oAuth2AccessToken.getAdditionalInformation().get("jti");
        final long timeout = oAuth2AccessToken.getExpiresIn();
        stringRedisUtils.set(redisKey, redisValue, timeout);
        return CommonResult.success(oauth2TokenDto);
    }

    @GetMapping("/test")
    public CommonResult<?> test() {
        return CommonResult.success();
    }

    @GetMapping("/query")
    @PreAuthorize("hasAuthority('res:query')")
    public CommonResult<?> query() {
        return CommonResult.success();
    }

}