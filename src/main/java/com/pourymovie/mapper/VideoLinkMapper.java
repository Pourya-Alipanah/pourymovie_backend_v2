package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateVideoLinkDto;
import com.pourymovie.dto.request.UpdateVideoLinkDto;
import com.pourymovie.dto.response.VideoLinkDto;
import com.pourymovie.entity.VideoLinkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VideoLinkMapper {

  VideoLinkDto toDto(VideoLinkEntity entity);

  List<VideoLinkDto> toDto(List<VideoLinkEntity> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "title", ignore = true)
  @Mapping(target = "episode", ignore = true)
  @Mapping(target = "url", ignore = true)
  VideoLinkEntity toEntity(CreateVideoLinkDto dto);

  List<VideoLinkEntity> toEntity(List<CreateVideoLinkDto> dtos);

  @Mapping(target = "title", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "episode", ignore = true)
  @Mapping(target = "url", ignore = true)
  void updateEntityFromDto(
      UpdateVideoLinkDto updateVideoLinkDto, @MappingTarget VideoLinkEntity entity);
}
