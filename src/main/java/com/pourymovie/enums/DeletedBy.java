package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum DeletedBy {
  USER("user"),
  ADMIN("admin");

  private final String displayName;

  DeletedBy(String displayName) {
    this.displayName = displayName;
  }
}
