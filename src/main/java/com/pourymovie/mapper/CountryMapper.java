package com.pourymovie.mapper;

import com.pourymovie.dto.response.CountryDetailsDto;
import com.pourymovie.dto.response.CountryDto;
import com.pourymovie.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CountryMapper {

  CountryDto toDto(CountryEntity countryEntity);

  CountryDetailsDto toDetailsDto(CountryEntity countryEntity);

  @Mapping(target = "titles", ignore = true)
  CountryEntity toEntity(CountryDto countryDto);

  default List<CountryDto> toDto(List<CountryEntity> countryEntities) {
    return countryEntities.stream().map(this::toDto).toList();
  }
}
