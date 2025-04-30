package com.test.lotterysystem;

import com.test.lotterysystem.Comment.Utils.SecurityUtil;
import org.junit.jupiter.api.Test;

public class SecurityTest {

    @Test
    public void encrypt(){
        String encrypt = SecurityUtil.encrypt("123456");
        System.out.println(encrypt);
    }

    @Test
    public void verify(){
        boolean verify = SecurityUtil.verify("123456", "24f1f6b4433745648b46f361062c3d13e10adc3949ba59abbe56e057f20f883e");
        System.out.println(verify);
    }
}
