package com.test.lotterysystem.Comment.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 设置验证码
    public boolean set(String key, String value) {
        try {
            // 可能会发生连接异常问题
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil 发生错误:{},{}", key, value, e);
            return false;
        }
    }

    public boolean set(String key, String value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil 发生错误:{},{},{}", key, value, time, e);
            return false;
        }
    }

    // 获取key
    public String get(String key) {
        try {
            return !StringUtils.hasText(key) ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("RedisUtil 发生错误:{}", key, e);
            return null;
        }
    }

    // 删除key
    public boolean delete(String... keys) {
        try {
            if (keys != null && keys.length > 0) {
                if (keys.length == 1) {
                    redisTemplate.delete(keys[0]);
                } else {
                    redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(keys));
                }
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil 发生错误:{}", keys, e);
            return false;
        }
    }

    // 判断是否存在指定的key
    public boolean hasKey(String key) {
        try {
            return StringUtils.hasText(key) ? redisTemplate.hasKey(key) : false;
        } catch (Exception e) {
            log.error("RedisUtil 发生错误:{}", key, e);
            return false;
        }
    }
}
