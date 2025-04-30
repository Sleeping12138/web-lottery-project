package com.test.lotterysystem.Controller.Param;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginParam implements Serializable {
    // 强制身份校验，如果不填则所有什么都可以使用
    private String mandatoryIdentity;
}
