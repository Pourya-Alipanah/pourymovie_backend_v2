package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum TitleType {
  Movie("movie"),
  Series("series");

  private final String typeName;

  TitleType(String typeName) {
    this.typeName = typeName;
  }
}
