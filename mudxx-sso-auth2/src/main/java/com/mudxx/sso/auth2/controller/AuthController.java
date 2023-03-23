package com.mudxx.sso.auth2.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.sso.auth2.entity.dto.LoginParamDto;
import com.mudxx.sso.auth2.service.AuthServiceImpl;
import com.mudxx.sso.auth2.utils.JwtTokenUtil;
import com.mudxx.sso.common.web.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    @ResponseBody
    public CommonResult<?> login(@RequestBody LoginParamDto loginParamDto){
        String token = authService.login(loginParamDto);
        if(StrUtil.isBlank(token)) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(JSONUtil.toJsonStr(authentication));
        return CommonResult.success(tokenMap);
    }

    @GetMapping("/token/check")
    @ResponseBody
    public CommonResult<?> check(@RequestParam("token") String token){
        if (StrUtil.isBlank(token)) {
            return CommonResult.validateFailed("token 为空");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(JSONUtil.toJsonStr(authentication));
        String username = jwtTokenUtil.getUserNameFromToken(token);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("username", username);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @GetMapping("/token/refresh")
    @ResponseBody
    public CommonResult<?> refresh(@RequestParam("token") String token){
        if (StrUtil.isBlank(token)) {
            return CommonResult.validateFailed("token 为空");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(JSONUtil.toJsonStr(authentication));
        if( ! jwtTokenUtil.canRefresh(token)) {
            return CommonResult.validateFailed("token 过期");
        }
        String refreshToken = jwtTokenUtil.refreshToken(token);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

}
