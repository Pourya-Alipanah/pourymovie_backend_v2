package com.pourymovie.enums;

public enum StreamBucketNames implements BucketName {
  TRAILER("pourymovie-trailer"),
  VIDEO("pourymovie-video");


  private final String bucketName;

  StreamBucketNames(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getBucketName() {
    return bucketName;
  }
}
