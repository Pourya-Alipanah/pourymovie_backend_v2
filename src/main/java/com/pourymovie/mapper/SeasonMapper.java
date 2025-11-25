package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateSeasonDto;
import com.pourymovie.dto.request.UpdateSeasonDto;
import com.pourymovie.dto.response.SeasonDto;
import com.pourymovie.entity.SeasonEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = VideoLinkMapper.class
)
public interface SeasonMapper {
  SeasonDto toDto(SeasonEntity entity);

  List<SeasonDto> toDto(List<SeasonEntity> entities);

  @Mappings({
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "episodes", ignore = true),
          @Mapping(target = "title", ignore = true)
  })
  SeasonEntity toEntity(CreateSeasonDto dto);

  @Mappings({
          @Mapping(target = "title", ignore = true),
          @Mapping(target = "id", ignore = true),
          @Mapping(target = "episodes", ignore = true)
  })
  void updateEntityFromDto(UpdateSeasonDto dto, @MappingTarget SeasonEntity entity);
}
