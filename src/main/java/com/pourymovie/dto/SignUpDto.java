package com.pourymovie.dto;

import com.pourymovie.validation.ConfirmPasswordMatches;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@ConfirmPasswordMatches
public class SignUpDto {
  @Size(min = 3, max = 96)
  private String firstName;

  @Size(min = 3, max = 96)
  private String lastName;

  @Email
  @NotEmpty
  private String email;

  @NotEmpty
  @Size(min = 8, max = 96)
  @Pattern(
          regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
          message = "Password must contain at least one letter, one number, and one special character (@$!%*#?&)"
  )
  private String password;

  @NotEmpty
  private String confirmPassword;

  @Valid
  private ConfirmUploadDto avatarUrl;
}
