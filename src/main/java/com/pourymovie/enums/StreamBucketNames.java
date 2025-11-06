package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum StreamBucketNames implements BucketName {
  TRAILER("pourymovie-trailer"),
  VIDEO("pourymovie-video");


  private final String bucketName;

  StreamBucketNames(String bucketName) {
    this.bucketName = bucketName;
  }

}
