package com.pourymovie.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetDto(@NotBlank @Email String email) {}
