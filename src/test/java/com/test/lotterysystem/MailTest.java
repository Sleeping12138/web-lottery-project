package com.test.lotterysystem;

import com.test.lotterysystem.Comment.Utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    public void test() {
        mailUtil.sendSampleMail("2523711871@qq.com","标题","正文");
        System.out.println("finish...");
    }
}
