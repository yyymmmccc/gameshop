package com.project.game.service;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.Map;

public interface RedisService {

    void setValues(String key, String value);

    void setValues(String key, String value, Duration duration);

    String getValues(String key);

    void deleteValues(String key);

    void expireValues(String key, int timeout);

    void setHashOps(String key, Map<String, String> value);

    String getHashOps(String key, String hashKey);

    void deleteHashOps(String key, String hashKey);

    boolean checkExistsValue(String value);
}
