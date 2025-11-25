package com.pourymovie.persistence;

import com.pourymovie.enums.UploadStatus;

public class UploadStatusConverter extends EnumConverter<UploadStatus>{
  public  UploadStatusConverter() {
    super(UploadStatus.class);
  }
}
