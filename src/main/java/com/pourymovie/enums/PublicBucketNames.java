package com.pourymovie.enums;

public enum PublicBucketNames implements BucketName {
  TRAILER("pourymovie-trailer"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail");

  private final String bucketName;

  PublicBucketNames(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getBucketName() {
    return bucketName;
  }
}
