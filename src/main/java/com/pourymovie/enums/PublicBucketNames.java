package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PublicBucketNames implements BaseAsymmetricEnum {
  TRAILER(AllBucketNames.TRAILER),
  PROFILE(AllBucketNames.PROFILE),
  COVER(AllBucketNames.COVER),
  THUMBNAIL(AllBucketNames.THUMBNAIL);

  @JsonValue private final AllBucketNames main;

  @Override
  public String getValue() {
    return main.getValue();
  }

  PublicBucketNames(AllBucketNames main) {
    this.main = main;
  }

  public static String[] allValues() {
    return Arrays.stream(values()).map(PublicBucketNames::getValue).toArray(String[]::new);
  }
}
