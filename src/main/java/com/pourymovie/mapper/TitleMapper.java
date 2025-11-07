package com.pourymovie.mapper;

import com.pourymovie.dto.response.TitleDetailsDto;
import com.pourymovie.dto.response.TitleDto;
import com.pourymovie.entity.TitleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper
public interface TitleMapper {

  TitleDto toDto(TitleEntity entity);

  TitleDetailsDto toDetailsDto(TitleEntity entity);

  default Page<TitleDto> toDtoPage(Page<TitleEntity> entities) {
    return entities.map(this::toDto);
  }
}
