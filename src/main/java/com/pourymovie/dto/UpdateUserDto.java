package com.pourymovie.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {
  @Size(min = 3, max = 96)
  private String firstName;

  @Size(min = 3, max = 96)
  private String lastName;

  @Email
  private String email;

  @Valid
  private ConfirmUploadDto avatarUrl;
}
