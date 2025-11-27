package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UploadFromEntity implements BaseAsymmetricEnum {
  TITLE("title"),
  PERSON("person"),
  USER("user"),
  VIDEO("video");

  @JsonValue private final String value;

  UploadFromEntity(String value) {
    this.value = value;
  }
}
