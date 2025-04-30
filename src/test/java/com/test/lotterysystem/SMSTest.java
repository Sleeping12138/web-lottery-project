package com.test.lotterysystem;

import com.test.lotterysystem.Comment.Utils.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SMSTest {
    @Autowired
    private SMSUtil smsUtil;

    @Test
    public void test(){
        smsUtil.sendMessage(
                "SMS_154950909",
                "19016414259",
                "{\"code\":\"1234\"}");
    }
}
