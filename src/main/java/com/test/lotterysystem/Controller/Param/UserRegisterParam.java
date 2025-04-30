package com.test.lotterysystem.Controller.Param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterParam {
    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotBlank(message = "邮箱不能为空")
    private String mail;

    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    private String password;

    @NotBlank(message = "用户身份不能为空")
    private String identity;
}
