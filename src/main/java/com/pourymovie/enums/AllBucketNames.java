package com.pourymovie.enums;

public enum AllBucketNames implements BucketName {
  AVATAR("pourymovie-avatar"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail"),
  TRAILER("pourymovie-trailer"),
  VIDEO("pourymovie-video");

  private final String bucketName;

  AllBucketNames(String bucketName) {
    this.bucketName = bucketName;
  }

  @Override
  public String getBucketName() {
    return bucketName;
  }
}
