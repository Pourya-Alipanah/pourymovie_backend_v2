package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmDto(
    @NotBlank String token,
    @NotBlank
        @Size(min = 8, max = 96)
        @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message =
                "Password must contain at least one letter, one number, and one special character (@$!%*#?&)")
        String newPassword) {}
