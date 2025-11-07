package com.pourymovie.enums;

import lombok.Getter;

@Getter
public enum UploadStatus {
  PENDING("pending"),
  CONFIRMED("confirmed");

  private String value;

  UploadStatus(String value){
    this.value = value;
  }
}
