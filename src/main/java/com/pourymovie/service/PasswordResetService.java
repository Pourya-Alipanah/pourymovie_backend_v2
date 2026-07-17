package com.pourymovie.service;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.request.PasswordResetConfirmDto;
import com.pourymovie.dto.request.PasswordResetDto;
import com.pourymovie.dto.response.Message;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.provider.EmailProvider;
import com.pourymovie.security.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
  private final UserService userService;
  private final StringRedisTemplate redisTemplate;
  private final EmailProvider emailProvider;
  private final UserSessionManager userSessionManager;
  private static final String RESET_TOKEN_PREFIX = "password:reset:";
  private final AppDefaults appDefaults;

  public Message requestPasswordReset(PasswordResetDto passwordResetDto) {
    UserEntity user = userService.getOptionalUserByEmail(passwordResetDto.email()).orElse(null);
    Message message =
        new Message("If the email exists in our system, a password reset link has been sent.");

    if (user == null) {
      return message;
    }

    String token = UUID.randomUUID().toString();
    String key = RESET_TOKEN_PREFIX + token;

    redisTemplate
        .opsForValue()
        .set(
            key,
            passwordResetDto.email(),
            appDefaults.getResetTokenTTlInMinutes(),
            TimeUnit.MINUTES);

    String resetLink = "%s?token=%s".formatted(appDefaults.getFrontendResetPasswordUrl(), token);

    emailProvider.sendPasswordResetEmail(passwordResetDto.email(), resetLink);

    return message;
  }

  @Transactional
  public Message confirmPasswordReset(PasswordResetConfirmDto passwordResetConfirmDto) {
    String key = RESET_TOKEN_PREFIX + passwordResetConfirmDto.token();

    String email = redisTemplate.opsForValue().get(key);

    if (email == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "The reset token is invalid or has expired.");
    }

    UserEntity user = userService.changePassword(email, passwordResetConfirmDto.newPassword());

    redisTemplate.delete(key);

    userSessionManager.revokeAllSessions(user.getId());

    return new Message("Your password has been successfully reset.");
  }
}
