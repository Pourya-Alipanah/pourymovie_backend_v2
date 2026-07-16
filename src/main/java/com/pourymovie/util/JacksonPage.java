package com.pourymovie.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JacksonPage<T> {
  private List<T> content;
  private int number;
  private int size;
  private long totalElements;
}
