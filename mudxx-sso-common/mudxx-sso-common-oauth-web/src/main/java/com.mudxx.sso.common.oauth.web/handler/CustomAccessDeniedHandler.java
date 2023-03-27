package com.mudxx.sso.common.oauth.web.handler;


import com.mudxx.sso.common.web.api.CommonResult;
import com.mudxx.sso.common.web.util.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author laiw
 * @date 2023/3/24 14:55
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CommonResult<Object> result = CommonResult.forbidden();
        WebUtils.writeJsonToClient(response, result);
    }
}

