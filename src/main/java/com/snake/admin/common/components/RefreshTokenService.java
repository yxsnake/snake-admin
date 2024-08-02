package com.snake.admin.common.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: snake
 * @create-time: 2024-07-23
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final String REFRESH_TOKEN_PREFIX = "refresh:token:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    public String generateRefreshToken(){
        // 随机数生成器
        SecureRandom random = new SecureRandom();

        // 生成一个32位长度的随机字符串
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        String refreshToken = sb.toString();
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken,String userId){
        String key = String.format(REFRESH_TOKEN_PREFIX,refreshToken);
        redisTemplate.opsForValue().set(key,userId);
    }

    public String getUserId(String refreshToken){
        String key = String.format(REFRESH_TOKEN_PREFIX,refreshToken);
        Object object = redisTemplate.opsForValue().get(key);
        return Objects.isNull(object)?null:String.valueOf(object);
    }

    public void removeRefreshToken(String refreshToken){
        String key = String.format(REFRESH_TOKEN_PREFIX,refreshToken);
        redisTemplate.delete(key);
    }


}
