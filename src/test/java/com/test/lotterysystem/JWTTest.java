package com.test.lotterysystem;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;

public class JWTTest {

    @Test
    public void getKey() {
        // 生成密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 将密钥编码为Base64字符串。
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
    }
}
