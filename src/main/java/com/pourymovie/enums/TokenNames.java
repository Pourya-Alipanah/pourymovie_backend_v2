package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum TokenNames {
  ACCESS_TOKEN("accessToken"),
  REFRESH_TOKEN("refreshToken");

  private final String tokenName;

  TokenNames(String tokenName) {
    this.tokenName = tokenName;
  }
}
