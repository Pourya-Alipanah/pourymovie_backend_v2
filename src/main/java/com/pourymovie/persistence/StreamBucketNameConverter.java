package com.pourymovie.persistence;

import com.pourymovie.enums.StreamBucketNames;

public class StreamBucketNameConverter extends EnumConverter<StreamBucketNames>{
  public  StreamBucketNameConverter() {
    super(StreamBucketNames.class);
  }
}
