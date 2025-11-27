package com.pourymovie.persistence;

import com.pourymovie.enums.VideoQuality;
import jakarta.persistence.Converter;

@Converter
public class VideoQualityConverter extends EnumConverter<VideoQuality> {
  public VideoQualityConverter() {
    super(VideoQuality.class);
  }
}
