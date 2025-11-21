package com.pourymovie.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record UpdatePeopleDto(
        String nameFa,
        String nameEn,
        String slug,
        LocalDate birthDate,
        LocalDate deathDate,
        LocalDate birthPlace,
        @Valid
        ConfirmUploadDto imageUrl
) {
}
