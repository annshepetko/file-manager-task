package com.ann.test.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.Base64;

/**
 * This is a simple service which provide method to write data to cache
 */
@Service
public class FileStorageService {

    private final RedisTemplate<String, String> redisTemplate;

    public FileStorageService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;

    }

    public void saveFile(String key, byte[] fileData, long ttlSeconds) {
        String base64Encoded = Base64.getEncoder().encodeToString(fileData);
        redisTemplate.opsForValue().set(key, base64Encoded, ttlSeconds, TimeUnit.SECONDS);
    }

    public Optional<byte[]> getFile(String key) {

        String base64Encoded = redisTemplate.opsForValue().get(key);
        if (base64Encoded != null) {
            return Optional.of(Base64.getDecoder().decode(base64Encoded));
        }
        return Optional.empty();
    }
}
