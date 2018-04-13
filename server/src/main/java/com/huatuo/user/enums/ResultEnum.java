package com.huatuo.user.enums;

import lombok.Getter;

/**
 * 结果返回枚举
 */
@Getter
public enum ResultEnum {
    LOGIN_FAIL(1,"登录失败"),
    ROLE_ERROR(2,"登录角色错误"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
