package com.test.lotterysystem.Controller.Param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserShortMessageLoginParam extends UserLoginParam{
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
}
