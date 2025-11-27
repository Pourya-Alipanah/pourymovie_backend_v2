package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateTitlePeopleDto;
import com.pourymovie.dto.response.TitlePeopleDto;
import com.pourymovie.entity.PeopleEntity;
import com.pourymovie.entity.TitlePeopleEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TitlePeopleMapper {
  TitlePeopleDto toDto(TitlePeopleEntity titlePeopleEntity);
  List<TitlePeopleDto> toDto(List<TitlePeopleEntity> titlePeopleEntities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "title", ignore = true)
  TitlePeopleEntity toEntity(CreateTitlePeopleDto dto, PeopleEntity person);
}
