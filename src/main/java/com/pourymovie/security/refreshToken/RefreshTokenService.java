package com.pourymovie.security.refreshToken;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            .expiresAt(LocalDateTime.now().plusMinutes(appDefaults.getDefaultRefreshTokenTTlInMinutes()))
            .build();
    return refreshTokenRepository.save(token);
  }

  public boolean isTokenValid(RefreshTokenEntity token) {
    return token.getExpiresAt().isAfter(LocalDateTime.now());
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
