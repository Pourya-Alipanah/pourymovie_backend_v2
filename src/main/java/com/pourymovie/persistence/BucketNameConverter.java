package com.pourymovie.persistence;

import com.pourymovie.enums.AllBucketNames;

public class BucketNameConverter extends EnumConverter<AllBucketNames>{
  public BucketNameConverter() {
    super(AllBucketNames.class);
  }
}
