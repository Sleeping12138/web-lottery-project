package com.test.lotterysystem.Comment.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    SUCCESS(200, "成功"),
    INTERNAL_SERVER_ERROR(500, "系统异常"),
    UNKNOWN(999, "未知错误");

    private Integer code;
    private String errMsg;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }
}
