package com.pourymovie.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
    @Size(min = 3, max = 96) String firstName,
    @Size(min = 3, max = 96) String lastName,
    @Email String email,
    @Valid ConfirmUploadDto avatarUrl) {}
