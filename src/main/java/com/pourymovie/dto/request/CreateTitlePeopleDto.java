package com.pourymovie.dto.request;

import com.pourymovie.enums.PersonRole;

public record CreateTitlePeopleDto(Long id, PersonRole role) {}
