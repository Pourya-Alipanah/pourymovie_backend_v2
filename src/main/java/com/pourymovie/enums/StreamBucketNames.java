package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum StreamBucketNames implements BaseAsymmetricEnum {
  TRAILER(AllBucketNames.TRAILER),
  VIDEO(AllBucketNames.VIDEO);

  @JsonValue
  private final AllBucketNames main;

  @Override
  public String getValue() {
    return main.getValue();
  }

  StreamBucketNames(AllBucketNames main) { this.main = main; }

}
