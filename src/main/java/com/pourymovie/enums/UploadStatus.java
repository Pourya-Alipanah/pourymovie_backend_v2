package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UploadStatus implements BaseAsymmetricEnum {
  PENDING("pending"),
  CONFIRMED("confirmed");

  @JsonValue private final String value;

  UploadStatus(String value) {
    this.value = value;
  }
}
