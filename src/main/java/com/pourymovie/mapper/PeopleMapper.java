package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreatePeopleDto;
import com.pourymovie.dto.request.UpdatePeopleDto;
import com.pourymovie.dto.response.PeopleDetailsDto;
import com.pourymovie.dto.response.PeopleDto;
import com.pourymovie.entity.PeopleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = { TitlePeopleMapper.class }
)
public interface PeopleMapper {

  PeopleDto toDto(PeopleEntity entity);

  @Mapping(target = "titles", source = "titlePersons")
  PeopleDetailsDto toDetailsDto(PeopleEntity entity);

  default Page<PeopleDto> toDto(Page<PeopleEntity> entities){
    return entities.map(this::toDto);
  }

  @Mapping(target = "titlePersons", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "imageUrl", ignore = true)
  PeopleEntity toEntity(CreatePeopleDto dto);

  @Mapping(target = "titlePersons", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "imageUrl", ignore = true)
  void updateEntityFromDto(UpdatePeopleDto dto, @MappingTarget PeopleEntity entity);
}
