package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateTitleDto;
import com.pourymovie.dto.request.UpdateTitleDto;
import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.entity.TitleEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TitleMapper {

  TitleDto toDto(TitleEntity entity);

  TitleDetailsDto toDetailsDto(TitleEntity entity);

  @Mappings({
          @Mapping(target = "writers", ignore = true),
          @Mapping(target = "writerLinks", ignore = true),
          @Mapping(target = "videoLinks", ignore = true),
          @Mapping(target = "seasons", ignore = true),
          @Mapping(target = "people", ignore = true),
          @Mapping(target = "language", ignore = true),
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "genres", ignore = true),
          @Mapping(target = "directors", ignore = true),
          @Mapping(target = "directorLinks", ignore = true),
          @Mapping(target = "country", ignore = true),
          @Mapping(target = "comments", ignore = true),
          @Mapping(target = "actors", ignore = true),
          @Mapping(target = "actorLinks", ignore = true),
          @Mapping(target = "trailerUrl", ignore = true),
          @Mapping(target = "thumbnailUrl", ignore = true),
          @Mapping(target = "coverUrl", ignore = true)
  })
  TitleEntity toEntity(CreateTitleDto dto);

  default Page<TitleDto> toDtoPage(Page<TitleEntity> entities) {
    return entities.map(this::toDto);
  }

  @Mappings({
          @Mapping(target = "writers", ignore = true),
          @Mapping(target = "writerLinks", ignore = true),
          @Mapping(target = "videoLinks", ignore = true),
          @Mapping(target = "seasons", ignore = true),
          @Mapping(target = "people", ignore = true),
          @Mapping(target = "language", ignore = true),
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "genres", ignore = true),
          @Mapping(target = "directors", ignore = true),
          @Mapping(target = "directorLinks", ignore = true),
          @Mapping(target = "country", ignore = true),
          @Mapping(target = "comments", ignore = true),
          @Mapping(target = "actors", ignore = true),
          @Mapping(target = "actorLinks", ignore = true),
          @Mapping(target = "trailerUrl", ignore = true),
          @Mapping(target = "thumbnailUrl", ignore = true),
          @Mapping(target = "coverUrl", ignore = true)
  })
  void updateEntityFromDto(UpdateTitleDto dto, @MappingTarget TitleEntity entity);
}
