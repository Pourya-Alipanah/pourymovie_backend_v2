package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PublicBucketNames implements BaseAsymmetricEnum {
  TRAILER("pourymovie-trailer"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail");

  @JsonValue
  private final String value;

  PublicBucketNames(String value) {
    this.value = value;
  }

}
