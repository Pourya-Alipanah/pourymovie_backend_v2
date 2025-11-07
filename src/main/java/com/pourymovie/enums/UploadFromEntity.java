package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum UploadFromEntity {
  TITLE("title"),
  PERSON("person"),
  USER("user"),
  VIDEO("video");

  private String value;

  UploadFromEntity(String value) {
    this.value = value;
  }
}
