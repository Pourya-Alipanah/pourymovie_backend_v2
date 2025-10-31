package com.pourymovie.service;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.SignInDto;
import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.security.JwtService;
import com.pourymovie.security.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
  private AuthenticationManager authenticationManager;

  @Autowired
  AppDefaults appDefaults;

  public void signIn(SignInDto signInDto, HttpServletResponse response) {
    try {
      var authToken = new UsernamePasswordAuthenticationToken(
              signInDto.getEmail(),
              signInDto.getPassword()
      );
      authenticationManager.authenticate(authToken);

      UserEntity user = userService.getUserByEmail(signInDto.getEmail());

      String accessToken = jwtService.generateAccessToken(user);

      RefreshTokenEntity refreshToken = refreshTokenService.generateRefreshToken(user);

      List<Cookie> tokens = generateTokenCookies(accessToken, refreshToken);


      for(Cookie cookie : tokens){
        response.addCookie(cookie);
      }

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);


    } catch (AuthenticationException ex) {
      throw new RuntimeException("Invalid credentials");
    }
  }

  private List<Cookie> generateTokenCookies(String accessToken, RefreshTokenEntity refreshToken) {
    int accessTokenExpiry = 60 * appDefaults.getDefaultAccessTokenTTlInMinutes();
    int refreshTokenExpiry = 60 * appDefaults.getDefaultRefreshTokenTTlInMinutes();

    Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
    accessTokenCookie.setHttpOnly(true);
    accessTokenCookie.setMaxAge(accessTokenExpiry);
    accessTokenCookie.setPath("/");

    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getToken());
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setMaxAge(refreshTokenExpiry);
    refreshTokenCookie.setPath("/");

    return List.of(accessTokenCookie, refreshTokenCookie);
  }

}
