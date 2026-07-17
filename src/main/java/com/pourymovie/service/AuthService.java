package com.pourymovie.service;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.request.SignInDto;
import com.pourymovie.dto.request.SignUpDto;
import com.pourymovie.dto.response.IpInfoResponse;
import com.pourymovie.dto.response.UserSession;
import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.TokenNames;
import com.pourymovie.enums.UserRole;
import com.pourymovie.security.jwt.JwtService;
import com.pourymovie.security.refreshToken.RefreshTokenService;
import com.pourymovie.security.session.UserSessionManager;
import com.pourymovie.util.CookieUtils;
import com.pourymovie.util.RequestMetadataUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final RefreshTokenService refreshTokenService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final AppDefaults appDefaults;
  private final UserSessionManager userSessionManager;

  public void signIn(
      SignInDto signInDto, HttpServletRequest request, HttpServletResponse response) {

    var authToken =
        new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password());
    authenticationManager.authenticate(authToken);

    UserEntity user = userService.getUserByEmail(signInDto.email());

    signAndSendTokens(request, response, user);
  }

  public void signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    UserEntity user = userService.createUser(signUpDto, UserRole.USER);

    signAndSendTokens(request, response, user);
  }

  public void handleOAuth2Success(
      OAuth2User oAuth2User, HttpServletRequest request, HttpServletResponse response) {
    String email = oAuth2User.getAttribute("email");
    UserEntity user =
        userService
            .getOptionalUserByEmail(email)
            .orElseGet(() -> userService.createUserForOAuth2(oAuth2User, UserRole.USER));

    signAndSendTokens(request, response, user);
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    String extractedRefreshToken =
        CookieUtils.getToken(TokenNames.REFRESH_TOKEN, request)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Refresh token is missing"));

    RefreshTokenEntity refreshToken = refreshTokenService.findByToken(extractedRefreshToken);

    boolean isTokenValid = refreshTokenService.isTokenValid(refreshToken);

    if (!isTokenValid) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

    UserEntity user = refreshToken.getUser();

    CookieUtils.getToken(TokenNames.ACCESS_TOKEN, request)
        .ifPresent(
            oldAccessToken -> {
              String oldJti = jwtService.extractJtiIgnoringExpiration(oldAccessToken);
              if (oldJti != null) {
                userSessionManager.revokeSession(user.getId(), oldJti);
              }
            });

    refreshTokenService.deleteByToken(extractedRefreshToken);

    signAndSendTokens(request, response, user);
  }

  public void signOut(HttpServletRequest request, HttpServletResponse response) {

    String extractedRefreshToken =
        CookieUtils.getToken(TokenNames.REFRESH_TOKEN, request)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Refresh token is missing"));

    refreshTokenService.deleteByToken(extractedRefreshToken);

    CookieUtils.getToken(TokenNames.ACCESS_TOKEN, request)
        .ifPresent(
            accessToken -> {
              String jti = jwtService.extractJtiIgnoringExpiration(accessToken);
              Long userId = jwtService.extractUserIdIgnoringExpiration(accessToken);
              if (userId != null && jti != null) {
                userSessionManager.revokeSession(userId, jti);
              }
            });

    var deleteCookies = CookieUtils.tokenToRemove(request, response);

    for (Cookie cookie : deleteCookies) {
      response.addCookie(cookie);
    }

    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  private void signAndSendTokens(
      HttpServletRequest request, HttpServletResponse response, UserEntity user) {

    int accessTokenExpiry = 60 * appDefaults.getDefaultAccessTokenTTlInMinutes();
    int refreshTokenExpiry = 60 * appDefaults.getDefaultRefreshTokenTTlInMinutes();
    String jti = UUID.randomUUID().toString();
    String ip = RequestMetadataUtils.getIpAddress(request);
    IpInfoResponse locationInfo =
        RequestMetadataUtils.getLocationFromIp(ip, appDefaults.getIpInfoToken());
    var deviceInfo = RequestMetadataUtils.getDeviceInfo(request);
    UserSession session =
        new UserSession(
            jti,
            ip,
            deviceInfo.device(),
            deviceInfo.os(),
            locationInfo,
            System.currentTimeMillis());

    userSessionManager.saveSession(user.getId(), session);

    String accessToken = jwtService.generateAccessToken(user, jti);

    RefreshTokenEntity refreshToken = refreshTokenService.generateRefreshToken(user);

    List<Cookie> tokens =
        CookieUtils.generateTokenCookies(
            accessToken, refreshToken, accessTokenExpiry, refreshTokenExpiry);

    for (Cookie cookie : tokens) {
      response.addCookie(cookie);
    }

    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
