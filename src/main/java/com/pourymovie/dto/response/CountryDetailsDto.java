package com.pourymovie.dto.response;

import java.util.List;

public record CountryDetailsDto(
        Long id,
        String nameFa,
        String nameEn,
        String slug,
        List<TitleDto> titles
) {
}
