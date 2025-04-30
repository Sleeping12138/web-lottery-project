package com.test.lotterysystem.Comment.Utils;

import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Slf4j
public class RegexUtil {
    // 检测邮箱格式
    public static boolean checkMail(String mail){
        if(!StringUtils.hasLength(mail)){
            log.error("邮箱为空");
            return false;
        }

        String regex = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return Pattern.matches(regex,mail);

//        return Validator.isEmail(mail);
    }

    // 检测手机号格式
    public static boolean checkPhone(String phoneNumber){
        if(!StringUtils.hasLength(phoneNumber)){
            log.error("手机号为空");
            return false;
        }

        String regex = "^1[3|4|5|6|7|8|9][0-9]{9}$";
        return Pattern.matches(regex,phoneNumber);

//        return Validator.isMobile(phoneNumber);
    }

    // 检测密码长度
    public static boolean checkPassword(String password){
        if (!StringUtils.hasLength(password)){
            log.error("密码为空");
            return false;
        }
        String regex= "^[0-9A-Za-z]{6,12}$";
        return Pattern.matches(regex,password);
    }
}
