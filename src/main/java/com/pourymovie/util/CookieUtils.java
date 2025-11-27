package com.pourymovie.util;

import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.enums.TokenNames;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CookieUtils {

  @Setter private static volatile boolean secureCookies = false;

  public static Optional<String> getToken(TokenNames tokenName, HttpServletRequest request) {
    String token = null;
    if (request.getCookies() != null) {
      for (var cookie : request.getCookies()) {
        if (tokenName.getTokenName().equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }
    return Optional.ofNullable(token);
  }

  private static Cookie generateTokenCookie(
      TokenNames tokenName, String token, int expiryTime, String path) {
    var cookie = new Cookie(tokenName.getTokenName(), token);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(expiryTime);
    cookie.setSecure(secureCookies);
    cookie.setPath(path);
    cookie.setAttribute("SameSite", "Lax");

    return cookie;
  }

  public static List<Cookie> tokenToRemove(
      HttpServletRequest request, HttpServletResponse response) {

    var tokenNamesToRemove =
        Set.of(TokenNames.ACCESS_TOKEN.getTokenName(), TokenNames.REFRESH_TOKEN.getTokenName());

    var cookiesToRemove = new ArrayList<Cookie>();

    if (request.getCookies() != null) {
      for (var cookie : request.getCookies()) {
        if (tokenNamesToRemove.contains(cookie.getName())) {
          cookie.setValue("");
          cookie.setMaxAge(0);
          cookie.setHttpOnly(true);
          cookie.setSecure(secureCookies);
          cookie.setPath(
              cookie.getName().equals(TokenNames.ACCESS_TOKEN.getTokenName())
                  ? "/"
                  : "/v2/auth/refresh-tokens");
          cookiesToRemove.add(cookie);
        }
      }
    }

    return cookiesToRemove;
  }

  public static List<Cookie> generateTokenCookies(
      String accessToken,
      RefreshTokenEntity refreshToken,
      int accessTokenExpiry,
      int refreshTokenExpiry) {

    Cookie accessTokenCookie =
        generateTokenCookie(TokenNames.ACCESS_TOKEN, accessToken, accessTokenExpiry, "/");

    Cookie refreshTokenCookie =
        generateTokenCookie(
            TokenNames.REFRESH_TOKEN,
            refreshToken.getToken(),
            refreshTokenExpiry,
            "/v2/auth/refresh-tokens");

    return List.of(accessTokenCookie, refreshTokenCookie);
  }
}
