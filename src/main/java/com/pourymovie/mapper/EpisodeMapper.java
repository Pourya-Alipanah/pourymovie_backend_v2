package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateEpisodeDto;
import com.pourymovie.dto.request.UpdateEpisodeDto;
import com.pourymovie.dto.response.EpisodeDto;
import com.pourymovie.entity.EpisodeEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = VideoLinkMapper.class
)
public interface EpisodeMapper {

  EpisodeDto toDto(EpisodeEntity entity);

  default List<EpisodeDto> toDto(List<EpisodeEntity> entities){
    return entities.stream().map(this::toDto).toList();
  }

  @Mapping(target = "videoLinks", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "season", ignore = true)
  EpisodeEntity toEntity(CreateEpisodeDto dto);

  @Mapping(target = "videoLinks", ignore = true)
  @Mapping(target = "season", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UpdateEpisodeDto dto, @MappingTarget EpisodeEntity entity);
}
