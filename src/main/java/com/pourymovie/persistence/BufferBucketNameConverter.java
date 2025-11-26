package com.pourymovie.persistence;

import com.pourymovie.enums.BufferBucketNames;

public class BufferBucketNameConverter extends EnumConverter<BufferBucketNames> {
  public BufferBucketNameConverter() {
    super(BufferBucketNames.class);
  }
}
