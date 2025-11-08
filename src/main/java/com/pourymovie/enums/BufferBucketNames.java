package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BufferBucketNames implements BaseAsymmetricEnum {
  AVATAR("pourymovie-avatar"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail");

  @JsonValue
  private final String value;

  BufferBucketNames(String value) {
    this.value = value;
  }
}
