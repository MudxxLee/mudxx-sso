package com.mudxx.sso.modules.resource.demo.web.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/3/30 14:00
 */
@Data
public class GetDto implements Serializable {
    private static final long serialVersionUID = 3850752686370649333L;
    private String name;
    private Integer age;
}
