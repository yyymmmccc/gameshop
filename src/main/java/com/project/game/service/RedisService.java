package com.project.game.service;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.Map;

public interface RedisService {

    void setValues(String key, String value);
    void setValues(String key, String value, Duration duration);
    String getValues(String key);
    void deleteValues(String key);
    public void setRecentViewGame(String email, int gameId);

}
