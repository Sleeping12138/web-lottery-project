package com.test.lotterysystem.Controller.Result;

import lombok.Data;

@Data
public class UserLoginResult {
    private String token;
    private String identity;
}
