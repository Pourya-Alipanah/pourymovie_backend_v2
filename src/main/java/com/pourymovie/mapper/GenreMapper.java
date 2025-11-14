package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateGenreDto;
import com.pourymovie.dto.request.UpdateGenreDto;
import com.pourymovie.dto.response.GenreDto;
import com.pourymovie.entity.GenreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GenreMapper {
  @Mapping(target = "titles", ignore = true)
  GenreEntity toEntity(GenreDto genreDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "titles", ignore = true)
  GenreEntity toEntity(CreateGenreDto genreDto);

  GenreDto toDto(GenreEntity genreEntity);

  default Page<GenreDto> toDtoPage(Page<GenreEntity> genreEntities){return genreEntities.map(this::toDto);};

  @Mapping(target = "titles", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UpdateGenreDto genreDto, @MappingTarget GenreEntity genreEntity);
}
