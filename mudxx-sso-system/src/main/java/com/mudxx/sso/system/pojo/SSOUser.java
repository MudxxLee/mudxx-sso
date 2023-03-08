package com.mudxx.sso.system.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
//@TableName(value = "sso-users")
public class SSOUser implements Serializable {
    private static final long serialVersionUID = 4831304712151465443L;
    //@TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String status;
}
