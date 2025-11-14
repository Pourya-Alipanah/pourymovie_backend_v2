package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateLanguageDto;
import com.pourymovie.dto.request.UpdateLanguageDto;
import com.pourymovie.dto.response.LanguageDto;
import com.pourymovie.entity.LanguageEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LanguageMapper {
  LanguageDto toDto(LanguageEntity entity);

  default Page<LanguageDto> toDto(Page<LanguageEntity> entities) {
    return entities.map(this::toDto);
  }

  @Mappings({
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "titles", ignore = true)
  })
  LanguageEntity toEntity(CreateLanguageDto dto);

  @Mapping(target = "titles", ignore = true)
  LanguageEntity toEntity(LanguageDto dto);

  @Mappings({
          @Mapping(target = "slug", ignore = true),
          @Mapping(target = "nameFa", ignore = true),
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "titles", ignore = true)
  })
  void updateEntityFromDto(UpdateLanguageDto dto, @MappingTarget LanguageEntity entity);
}
