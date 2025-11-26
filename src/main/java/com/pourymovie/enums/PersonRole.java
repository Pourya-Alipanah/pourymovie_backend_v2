package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PersonRole implements BaseAsymmetricEnum {
  ACTOR("actor"),
  DIRECTOR("director"),
  WRITER("writer");

  @JsonValue private final String value;

  PersonRole(String value) {
    this.value = value;
  }
}
