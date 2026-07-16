package com.pourymovie.security.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.response.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSessionManager {

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;
  private final AppDefaults appDefaults;

  private static final String HASH_KEY_PREFIX = "user:sessions:";

  public void saveSession(Long userId, UserSession session) {
    String key = HASH_KEY_PREFIX + userId;
    try {
      String sessionJson = objectMapper.writeValueAsString(session);
      redisTemplate.opsForHash().put(key, session.jti(), sessionJson);

      redisTemplate.expire(key, appDefaults.getDefaultRefreshTokenTTlInMinutes(), TimeUnit.MINUTES);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize user session to JSON", e);
    }
  }

  public boolean isSessionActive(Long userId, String jti) {
    String key = HASH_KEY_PREFIX + userId;
    return redisTemplate.opsForHash().hasKey(key, jti);
  }

  public List<UserSession> getActiveSessions(Long userId) {
    String key = HASH_KEY_PREFIX + userId;
    Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

    return entries.values().stream()
        .map(
            value -> {
              try {
                return objectMapper.readValue((String) value, UserSession.class);
              } catch (JsonProcessingException e) {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public void revokeSession(Long userId, String jti) {
    String key = HASH_KEY_PREFIX + userId;
    redisTemplate.opsForHash().delete(key, jti);
  }

  public void revokeAllOtherSessions(Long userId, String currentJti) {
    String key = HASH_KEY_PREFIX + userId;
    List<UserSession> sessions = getActiveSessions(userId);

    for (UserSession session : sessions) {
      if (!session.jti().equals(currentJti)) {
        redisTemplate.opsForHash().delete(key, session.jti());
      }
    }
  }

  public void revokeAllSessions(Long userId) {
    String key = HASH_KEY_PREFIX + userId;
    redisTemplate.delete(key);
  }
}
