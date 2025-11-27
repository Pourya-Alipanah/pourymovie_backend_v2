package com.pourymovie.mapper;

import com.pourymovie.dto.response.CountryDetailsDto;
import com.pourymovie.dto.response.CountryDto;
import com.pourymovie.entity.CountryEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
