package com.mudxx.sso.modules.resource.demo.web.api.controller;

import cn.hutool.json.JSONUtil;
import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.modules.resource.demo.web.api.dto.GetDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @GetMapping("/select")
    public String select(){
        return "Select Resource ok";
    }

    @GetMapping("/query")
    @PreAuthorize("hasAuthority('resource:query')")
    public String query(){
        return "Query Resource ok";
    }

    @GetMapping("/get")
    public CommonResult<?> get(@ModelAttribute GetDto getDto){
        System.out.println(JSONUtil.toJsonStr(getDto));
        return CommonResult.success(getDto);
    }

}
