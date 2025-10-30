package com.pourymovie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pourymovie")
public class AppDefaults {
  private int defaultPageSize;
  private int defaultPageNumber;
}