package com.pourymovie.dto.response;

public record PeopleDto(
        Long id,
        String nameFa,
        String nameEn,
        String slug,
        String birthDate,
        String deathDate,
        String birthPlace,
        String imageUrl
) {}
