package com.pourymovie.service;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.SignInDto;
import com.pourymovie.dto.SignUpDto;
import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.TokenNames;
import com.pourymovie.enums.UserRole;
import com.pourymovie.security.JwtService;
import com.pourymovie.security.RefreshTokenService;
import com.pourymovie.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
  @Autowired
  private UserService userService;
  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private DaoAuthenticationProvider authenticationManager;
  @Autowired
  private AppDefaults appDefaults;


  public void signIn(SignInDto signInDto, HttpServletResponse response) {
    try {
      var authToken = new UsernamePasswordAuthenticationToken(
              signInDto.getEmail(),
              signInDto.getPassword()
      );
      authenticationManager.authenticate(authToken);

      UserEntity user = userService.getUserByEmail(signInDto.getEmail());

      signAndSendTokens(response, user);

    } catch (AuthenticationException ex) {
      throw new RuntimeException("Invalid credentials");
    }
  }

  public void signUp(SignUpDto signUpDto, HttpServletResponse response) {
    try {

      UserEntity user = userService.createUser(signUpDto , UserRole.USER);

      signAndSendTokens(response, user);

    } catch (AuthenticationException ex) {
      throw new RuntimeException(ex);
    }
  }

  public void refreshToken(HttpServletRequest request , HttpServletResponse response) {
    try{
      String extractedRefreshToken = CookieUtils.getToken(TokenNames.REFRESH_TOKEN, request)
              .orElseThrow(() -> new RuntimeException("Refresh token is missing"));

      RefreshTokenEntity refreshToken = refreshTokenService.findByToken(extractedRefreshToken);

      boolean isTokenValid = refreshTokenService.isTokenValid(refreshToken);

      if(!isTokenValid){
        throw new RuntimeException("Refresh token is expired");
      }

      UserEntity user = refreshToken.getUser();

      refreshTokenService.deleteByToken(extractedRefreshToken);

      signAndSendTokens(response, user);

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public void signOut(HttpServletRequest request , HttpServletResponse response) {
    try{
      String extractedRefreshToken = CookieUtils.getToken(TokenNames.REFRESH_TOKEN, request)
              .orElseThrow(() -> new RuntimeException("Refresh token is missing"));

      refreshTokenService.deleteByToken(extractedRefreshToken);

      var deleteCookies = CookieUtils.tokenToRemove(request,response);

      for(Cookie cookie : deleteCookies){
        response.addCookie(cookie);
      }

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void signAndSendTokens(HttpServletResponse response, UserEntity user) {

    int accessTokenExpiry = 60 * appDefaults.getDefaultAccessTokenTTlInMinutes();
    int refreshTokenExpiry = 60 * appDefaults.getDefaultRefreshTokenTTlInMinutes();

    String accessToken = jwtService.generateAccessToken(user);

    RefreshTokenEntity refreshToken = refreshTokenService.generateRefreshToken(user);

    List<Cookie> tokens = CookieUtils.generateTokenCookies(accessToken, refreshToken, accessTokenExpiry, refreshTokenExpiry);

    for(Cookie cookie : tokens){
      response.addCookie(cookie);
    }

    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

}
