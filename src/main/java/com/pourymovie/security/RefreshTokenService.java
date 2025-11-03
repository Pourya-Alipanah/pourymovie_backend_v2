package com.pourymovie.security;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

  @Autowired
  private AppDefaults appDefaults;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenEntity generateRefreshToken(UserEntity user) {
    RefreshTokenEntity token = RefreshTokenEntity.builder()
            .user(user)
            .token(UUID.randomUUID().toString())
            .expiresAt(Instant.now().plusMillis(1000L * 60 * appDefaults.getDefaultRefreshTokenTTlInMinutes()))
            .build();
    return refreshTokenRepository.save(token);
  }

  public boolean isTokenValid(RefreshTokenEntity token) {
    return token.getExpiresAt().isAfter(Instant.now());
  }

  public void deleteByUser(UserEntity user) {
    refreshTokenRepository.deleteByUser(user);
  }
  public void deleteByToken(String token) {
      refreshTokenRepository.deleteByToken(token);

  }

  public RefreshTokenEntity findByToken(String token) {
    return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Refresh token not found"));
  }
}
