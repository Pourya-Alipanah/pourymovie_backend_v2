package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateSeasonDto;
import com.pourymovie.dto.response.SeasonDto;
import com.pourymovie.entity.SeasonEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SeasonMapper {
  SeasonDto toDto(SeasonEntity entity);

  List<SeasonDto> toDto(List<SeasonEntity> entities);

  SeasonEntity toEntity(CreateSeasonDto dto);
}
