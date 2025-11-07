package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UploadType implements BaseAsymmetricEnum{
  AVATAR("avatar"),
  PROFILE("profile"),
  COVER("cover"),
  THUMBNAIL("thumbnail"),
  TRAILER("trailer"),
  VIDEO("video");

  @JsonValue
  private final String value;

  UploadType(String value) {
    this.value = value;
  }

}
