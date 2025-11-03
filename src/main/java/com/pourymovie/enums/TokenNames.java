package com.pourymovie.enums;

public enum TokenNames {
  ACCESS_TOKEN("accessToken"),
  REFRESH_TOKEN("refreshToken");

  private final String tokenName;

  TokenNames(String tokenName) {
    this.tokenName = tokenName;
  }

  public String getTokenName() {
    return tokenName;
  }
}
