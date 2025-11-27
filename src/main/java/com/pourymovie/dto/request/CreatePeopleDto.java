package com.pourymovie.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;

public record CreatePeopleDto(
    @NotEmpty String nameFa,
    @NotEmpty String nameEn,
    @NotEmpty String slug,
    @NotEmpty LocalDate birthDate,
    LocalDate deathDate,
    @NotEmpty LocalDate birthPlace,
    @Valid ConfirmUploadDto imageUrl) {}
