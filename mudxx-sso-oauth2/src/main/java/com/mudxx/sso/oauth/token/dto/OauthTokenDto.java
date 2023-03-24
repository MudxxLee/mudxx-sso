package com.mudxx.sso.oauth.token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/3/23 15:42
 */
@Data
public class OauthTokenDto implements Serializable {
    private static final long serialVersionUID = 7654156221953209933L;

    @JsonProperty(value = "grant_type")
    private String grantType;

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    private String username;

    private String password;


}
