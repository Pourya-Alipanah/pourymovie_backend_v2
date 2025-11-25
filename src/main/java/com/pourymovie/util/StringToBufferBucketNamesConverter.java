package com.pourymovie.util;

import com.pourymovie.enums.BufferBucketNames;
import org.springframework.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBufferBucketNamesConverter implements Converter<String, BufferBucketNames> {
  @Override
  public BufferBucketNames convert(@NonNull String source) {
    for (BufferBucketNames bucket : BufferBucketNames.values()) {
      if (bucket.getValue().equals(source) || bucket.name().equalsIgnoreCase(source)) {
        return bucket;
      }
    }
    throw new IllegalArgumentException("Invalid bucket: " + source);
  }
}
