package com.test.lotterysystem;

import com.test.lotterysystem.Comment.Utils.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class GlobalTest {
    @Test
    public void test(){
        HashMap<String, String> map = new HashMap<>();
        map.put("name","zhangsna");
        System.out.println(JacksonUtil.writeValueAsString(map));
    }
}
