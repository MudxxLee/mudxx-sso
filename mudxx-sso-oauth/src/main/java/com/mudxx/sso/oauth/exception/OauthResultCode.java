package com.mudxx.sso.oauth.exception;

import com.mudxx.sso.common.web.api.IErrorCode;

/**
 * @author laiw
 * @description 处理失败枚举
 * @date 2023/3/23 16:04
 */
public enum OauthResultCode implements IErrorCode {

    /**
     * 请求缺少某个必需参数，或者格式不正确
     */
    INVALID_REQUEST(96002, "请求缺少某个必需参数，或者格式不正确"),

    /**
     * client_id 和client_secret不匹配
     */
    INVALID_CLIENT(96003, "client_id和client_secret不匹配"),

    /**
     * grant是无效的
     */
    INVALID_GRANT(96004, "grant/refresh token/code无效"),

    /**
     * 客户端没有权限使用该请求, 需要在开放平台申请相关权限
     */
    UNAUTHORIZED_CLIENT(96005, "客户端没有权限使用该请求, 需要在开放平台申请相关权限"),

    /**
     * grant不被授权服务器所支持
     */
    UNSUPPORTED_GRANT_TYPE(96006, "grant不被授权服务器所支持"),

    /**
     * 请求的作用域是无效的、未知的、格式不正确的，或超出了之前许可的作用域
     */
    INVALID_SCOPE(96007, "请求的作用域是无效的、未知的、格式不正确的，或超出了之前许可的作用域"),

    /**
     * access token 是错误的或已经过期
     */
    INVALID_TOKEN(96008, "access/refresh token 是错误的或已经过期"),

    /**
     * 用户名或密码错误
     */
    USERNAME_PASSWORD(96009, "用户名或密码错误"),

    /**
     * 响应类型不为授权服务器所支持
     */
    UNSUPPORTED_RESPONSE_TYPE(96011, "响应类型不为授权服务器所支持"),

    /**
     * 用户或授权服务器拒绝了请求, 可能是用户取消了对应用的授权或者请求参数的mac签名验证错误
     */
    ACCESS_DENIED(96012, "用户或授权服务器拒绝了请求, 可能是用户取消了对应用的授权或者请求参数的mac签名验证错误"),

    /**
     * 重定向uri不匹配
     */
    REDIRECT_URI_MISMATCH(96013, "重定向uri不匹配"),

    /**
     * 系统错误
     */
    UNAUTHORIZED(96099, "OAuth Error");

    private final long code;

    private final String message;

    private OauthResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
