package com.test.lotterysystem.Comment.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ErrorCode {
    Integer getCode();
    String getErrMsg();
}
