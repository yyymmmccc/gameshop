package com.project.game.service.Impl;

import com.project.game.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 유효기간을 설정하면서 키를 생성
    public void setValues(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();

        if (redisTemplate.opsForValue().get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void setRecentViewGame(String email, int gameId){
        double score = System.currentTimeMillis();
        redisTemplate.opsForZSet().add("recent_view_" + email, String.valueOf(gameId), score);
    }

    public List<Integer> getRecentProductView(String email){

        Set<Object> recentViewSet = redisTemplate.opsForZSet().reverseRange("recent_view_" + email, 0, -1);

        // Set<Object>를 List<Integer>로 변환
        return recentViewSet.stream()
                .map(Object::toString)      // Object를 String으로 변환
                .map(Integer::parseInt)     // String을 Integer로 변환
                .collect(Collectors.toList()); // List<Integer>로 수집
    }
}
