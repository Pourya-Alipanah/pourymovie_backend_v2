package com.pourymovie.dto;

import com.pourymovie.enums.AllBucketNames;
import com.pourymovie.validation.ValidBucketName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ConfirmUploadDto {

  @ValidBucketName(enumClass = AllBucketNames.class)
  @NotEmpty
  private String bucketName;

  @NotEmpty
  private String key;
}
