package com.mudxx.sso.auth.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SSOUser implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String status;
    private List<String> permissions;
}
