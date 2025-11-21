package com.pourymovie.dto.response;

import com.pourymovie.enums.PersonRole;

public record TitlePeopleDto(
        Long id,
        PersonRole role,
        TitleSummaryDto title
) {
}
