package com.pourymovie.dto.request;

import com.pourymovie.validation.ConfirmPasswordMatches;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@ConfirmPasswordMatches
public record SignUpDto(
    @Size(min = 3, max = 96) String firstName,
    @Size(min = 3, max = 96) String lastName,
    @Email @NotEmpty String email,
    @NotEmpty
        @Size(min = 8, max = 96)
        @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message =
                "Password must contain at least one letter, one number, and one special character (@$!%*#?&)")
        String password,
    @NotEmpty String confirmPassword,
    @Valid ConfirmUploadDto avatarUrl) {}
