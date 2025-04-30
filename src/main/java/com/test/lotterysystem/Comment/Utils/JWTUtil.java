package com.test.lotterysystem.Comment.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 使用JWT令牌 + token
@Slf4j
public class JWTUtil {
    private static final String SECRET = "QpVYNeb+HhpnNEGbkrUfF0X1g83Za4nUUK3MGpA4tnY=";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(SECRET));
    private static final long EXPIRATION = 60 * 60 * 1000;

    /**
     * 生成密钥
     *
     * @param claim
     * @return
     */
    public static String genJwt(Map<String, Object> claim) {
        //签名算法
        String jwt = Jwts.builder()
                .setClaims(claim)             // 自定义内容(载荷)
                .setIssuedAt(new Date())      // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 设置过期时间
                .signWith(SECRET_KEY)         // 签名算法
                .compact();
        return jwt;
    }

    /**
     * 验证密钥
     */
    public static Claims parseJWT(String jwt) {
        if (!StringUtils.hasLength(jwt)) {
            return null;
        }
        // 创建解析器, 设置签名密钥
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder().setSigningKey(SECRET_KEY);
        Claims claims = null;
        try {
            //解析token
            claims = jwtParserBuilder.build().parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            // 签名验证失败
            log.error("解析令牌错误,jwt:{}", jwt, e);
        }
        return claims;

    }

    /**
     * 从token中获取用户信息
     */
    public static Integer getUserIdFromToken(String jwtToken) {
        Claims claims = JWTUtil.parseJWT(jwtToken);
        if (claims != null) {
            Map<String, Object> userInfo = new HashMap<>(claims);
            return (Integer) userInfo.get("userId");
        }
        return null;
    }
}
