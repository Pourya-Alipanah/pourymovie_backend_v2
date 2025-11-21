package com.pourymovie.mapper;

import com.pourymovie.dto.response.TitlePeopleDto;
import com.pourymovie.entity.TitlePeopleEntity;
import org.mapstruct.Mapper;

@Mapper
public interface TitlePeopleMapper {
  TitlePeopleDto toDto(TitlePeopleEntity titlePeopleEntity);
}
