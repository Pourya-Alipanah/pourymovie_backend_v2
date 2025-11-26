package com.pourymovie.util;

import com.pourymovie.entity.RefreshTokenEntity;
import com.pourymovie.enums.TokenNames;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CookieUtils {

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

  private static Cookie generateTokenCookie(TokenNames tokenName, String token, int expiryTime) {
    var cookie = new Cookie(tokenName.getTokenName(), token);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(expiryTime);
    cookie.setPath("/");
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
          cookie.setPath("/");
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
        generateTokenCookie(TokenNames.ACCESS_TOKEN, accessToken, accessTokenExpiry);

    Cookie refreshTokenCookie =
        generateTokenCookie(TokenNames.REFRESH_TOKEN, refreshToken.getToken(), refreshTokenExpiry);

    return List.of(accessTokenCookie, refreshTokenCookie);
  }
}
