package com.pourymovie.util;

import com.pourymovie.enums.StreamBucketNames;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStreamBucketNamesConverter implements Converter<String, StreamBucketNames> {
  @Override
  public StreamBucketNames convert(@NotNull String source) {
    for (StreamBucketNames bucket : StreamBucketNames.values()) {
      if (bucket.getValue().equals(source) || bucket.name().equalsIgnoreCase(source)) {
        return bucket;
      }
    }
    throw new IllegalArgumentException("Invalid bucket: " + source);
  }
}

