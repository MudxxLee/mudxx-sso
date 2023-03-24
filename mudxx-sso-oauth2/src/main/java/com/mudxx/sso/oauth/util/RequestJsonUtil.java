package com.mudxx.sso.oauth.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author laiw
 * @description 获取request   json参数
 * @date 2023/3/23 16:23
 */
public class RequestJsonUtil {
    public static JSONObject getRequestJsonObject(HttpServletRequest request) throws IOException {
        String json = getRequestJsonString(request);
        return JSONUtil.parseObj(json);
    }

    private RequestJsonUtil() {
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     */
    public static String getRequestJsonString(HttpServletRequest request)
            throws IOException {
        String submitMethod = request.getMethod();
        // GET
        if (HttpMethod.GET.name().equals(submitMethod)) {
            return new String(request.getQueryString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
                    .replaceAll("%22", "\"");
            // POST
        } else {
            return getRequestPostStr(request);
        }
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return new byte[0];
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readLen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readLen == -1) {
                break;
            }
            i += readLen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }
}

