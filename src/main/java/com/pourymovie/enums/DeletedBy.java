package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DeletedBy implements BaseAsymmetricEnum {
  USER("user"),
  ADMIN("admin");

  @JsonValue private final String value;

  DeletedBy(String value) {
    this.value = value;
  }
}
