package com.pourymovie.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignInDto(
        @Email
        @NotEmpty
        String email,

        @NotEmpty
        String password
) {
}
