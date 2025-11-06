package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum PublicBucketNames implements BucketName {
  TRAILER("pourymovie-trailer"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail");

  private final String bucketName;

  PublicBucketNames(String bucketName) {
    this.bucketName = bucketName;
  }

}
