package com.test.lotterysystem.Comment.Utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SecurityUtil {
    public static String encrypt(String password){
        String salt = UUID.randomUUID().toString().replace("-", "");
        String temp = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        return salt+temp;
    }

    public static boolean verify(String password,String sqlPassword){
        String salt = sqlPassword.substring(0, 32);
        String temp = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        return (salt+temp).equals(sqlPassword);
    }
}
