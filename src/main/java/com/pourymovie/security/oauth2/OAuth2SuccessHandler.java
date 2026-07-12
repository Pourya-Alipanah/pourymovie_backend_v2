package com.pourymovie.security.oauth2;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final AuthService authService;
  private final AppDefaults appDefaults;

  public OAuth2SuccessHandler(@Lazy AuthService authService, AppDefaults appDefaults) {
    this.authService = authService;
    this.appDefaults = appDefaults;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    authService.handleOAuth2Success(oAuth2User, response);
    response.sendRedirect(appDefaults.getFrontendOauthRedirectUrl());
  }
}
