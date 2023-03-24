package com.mudxx.sso.oauth.service.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author laiwen
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = 928313511528555637L;
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private List<String> permissions;
}
