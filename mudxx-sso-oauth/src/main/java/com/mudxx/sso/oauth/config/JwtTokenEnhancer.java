package com.mudxx.sso.oauth.config;

import com.mudxx.sso.oauth.service.vo.UserVo;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置扩展jwt存储内容
 * @author laiw
 * @date 2023/3/20 10:44
 */
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserVo userVo = (UserVo) authentication.getPrincipal();
        Map<String, Object> info = new HashMap<>();
        info.put("username", userVo.getUsername());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
