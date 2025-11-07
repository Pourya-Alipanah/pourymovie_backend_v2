package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum StreamBucketNames implements BaseAsymmetricEnum {
  TRAILER("pourymovie-trailer"),
  VIDEO("pourymovie-video");

  @JsonValue
  private final String value;

  StreamBucketNames(String value) {
    this.value = value;
  }

}
