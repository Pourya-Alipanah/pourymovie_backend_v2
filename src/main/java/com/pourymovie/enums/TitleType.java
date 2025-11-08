package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TitleType implements BaseAsymmetricEnum{
  Movie("movie"),
  Series("series");

  @JsonValue
  private final String value;

  TitleType(String value) {
    this.value = value;
  }
}
