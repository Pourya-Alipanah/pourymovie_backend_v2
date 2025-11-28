package com.pourymovie.dto.request;

import com.pourymovie.enums.TitleType;

import java.util.List;

public record TitleFilterDto(
    String title,
    TitleType type,
    List<String> genres,
    List<String> actors,
    List<String> directors,
    List<String> writers,
    List<String> countries,
    Integer minYear,
    Integer maxYear,
    Float minRating,
    Float maxRating,
    Boolean isTop250,
    Boolean hasSubtitle) {}
