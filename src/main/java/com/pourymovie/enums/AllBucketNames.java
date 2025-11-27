package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AllBucketNames implements BaseAsymmetricEnum {
  AVATAR("pourymovie-avatar"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail"),
  TRAILER("pourymovie-trailer"),
  VIDEO("pourymovie-video");

  @JsonValue private final String value;

  AllBucketNames(String value) {
    this.value = value;
  }
}
