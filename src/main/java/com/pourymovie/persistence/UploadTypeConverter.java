package com.pourymovie.persistence;

import com.pourymovie.enums.UploadType;

public class UploadTypeConverter extends EnumConverter<UploadType>{
  public  UploadTypeConverter() {
    super(UploadType.class);
  }
}
