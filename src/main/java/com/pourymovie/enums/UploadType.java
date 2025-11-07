package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum UploadType {
  AVATAR("avatar"),
  PROFILE("profile"),
  COVER("cover"),
  THUMBNAIL("thumbnail"),
  TRAILER("trailer"),
  VIDEO("video");

  private String value;

  UploadType(String value) {
    this.value = value;
  }

}
