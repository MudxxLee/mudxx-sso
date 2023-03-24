package com.mudxx.sso.common.web.api;

/**
 * 封装API的错误码
 *
 * @author laiwen
 * @date 2019/4/19
 */
public interface IErrorCode {

    long getCode();

    String getMessage();
}
