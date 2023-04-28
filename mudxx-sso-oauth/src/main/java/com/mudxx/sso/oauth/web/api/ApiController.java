package com.mudxx.sso.oauth.web.api;

import com.mudxx.sso.common.web.api.CommonResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

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