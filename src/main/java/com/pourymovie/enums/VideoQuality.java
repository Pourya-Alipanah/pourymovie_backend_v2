package com.pourymovie.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum VideoQuality implements BaseAsymmetricEnum {
  Q240p("240p"),
  Q360p("360p"),
  Q480p("480p"),
  Q720p("720p"),
  Q1080p("1080p"),
  Q1440p("1440p"),
  Q2160p("2160p"),
  Q4320p("4320p"),
  WEB_DL("WEB-DL"),
  WEBRip("WEBRip"),
  BluRay("BluRay"),
  HDRip("HDRip"),
  DVDRip("DVDRip"),
  CAM("CAM"),
  TS("TS"),
  HDTV("HDTV");

  @JsonValue private final String value;

  VideoQuality(String value) {
    this.value = value;
  }
}
