package com.test.lotterysystem.Comment.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ControllerErrorCode implements ErrorCode{
    TRANS_ERROR(301,"转换参数为空");

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
