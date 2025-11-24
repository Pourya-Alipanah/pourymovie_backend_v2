package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BufferBucketNames implements BaseAsymmetricEnum {
  AVATAR(AllBucketNames.AVATAR),
  PROFILE(AllBucketNames.PROFILE),
  COVER(AllBucketNames.COVER),
  THUMBNAIL(AllBucketNames.THUMBNAIL);

//  @JsonValue
//  private final String value;

  @JsonValue
  private final AllBucketNames main;

  @Override
  public String getValue() {
    return main.getValue();
  }

  BufferBucketNames(AllBucketNames main) { this.main = main; }

//  BufferBucketNames(String value) {
//    this.value = value;
//  }
}
