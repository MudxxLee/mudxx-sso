package com.mudxx.sso.oauth.http;


import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author laiw
 * @description 重写 HttpServletRequestWrapper
 * @date 2023/3/23 16:22
 */
public class OauthHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * 用于存储请求参数
     */
    private final Map<String, String[]> params = new HashMap<>();

    /**
     *缓存请求体  防止多次使用请求体
     */
    private byte[] requestBody = null;

    private final Map<String, String> headerMap = new HashMap<>();

    /**
     * 构造方法
     *
     * @param request 请求
     * @throws IOException 异常
     */
    public OauthHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        //缓存请求body
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 把请求参数添加到我们自己的map当中
        this.params.putAll(request.getParameterMap());
    }

    /**
     * 重写 getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (requestBody == null) {
            requestBody = new byte[0];
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    /**
     * 重写 getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


    /**
     * 添加参数到map中
     */
    public void setParameterMap(Map<String, Object> extraParams) {
        for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加参数到map中
     */
    public void setParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }

    /**
     * 重写getParameter，代表参数从当前类中的map获取
     */
    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    /**
     * 重写getParameterValues方法，从当前类的 map中取值
     */
    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

    /**
     * add a header with given name and value
     */
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(headerMap.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }
}

