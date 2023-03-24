package com.mudxx.sso.common.web.util;

import com.mudxx.sso.common.web.api.CommonResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author laiw
 * @date 2023/3/20 11:07
 */
public class WebUtils {

    public static void writeJsonToClient(HttpServletResponse response, CommonResult<?> result) throws IOException {
        //设置响应数据编码和响应数据类型
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out= response.getWriter();
        out.write(result.toString());
        out.flush();
    }

}
