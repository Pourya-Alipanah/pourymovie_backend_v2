package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum PersonRole {
  ACTOR("actor"),

  DIRECTOR("director"),

  WRITER("writer");

  private final String displayName;

  PersonRole(String displayName) {
    this.displayName = displayName;
  }
}
