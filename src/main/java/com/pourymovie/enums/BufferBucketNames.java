package com.pourymovie.enums;

public enum BufferBucketNames implements BucketName {
  AVATAR("pourymovie-avatar"),
  PROFILE("pourymovie-profile"),
  COVER("pourymovie-cover"),
  THUMBNAIL("pourymovie-thumbnail");


  private final String bucketName;

  BufferBucketNames(String bucketName) {
    this.bucketName = bucketName;
  }
  @Override
  public String getBucketName() {
    return bucketName;
  }
}
