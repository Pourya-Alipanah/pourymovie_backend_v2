package com.pourymovie.mapper;

import com.pourymovie.dto.request.CreateCommentDto;
import com.pourymovie.dto.request.UpdateCommentDto;
import com.pourymovie.dto.response.CommentDto;
import com.pourymovie.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
  @Mapping(target = "isUpdated", ignore = true)
  CommentDto toDto(CommentEntity commentEntity);

  default Page<CommentDto> toDto(Page<CommentEntity> commentEntityPage) {
    return commentEntityPage.map(this::toDto);
  }

  @Mapping(target = "user", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updateAt", ignore = true)
  @Mapping(target = "title", ignore = true)
  @Mapping(target = "deletedBy", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  CommentEntity toEntity(CreateCommentDto commentDto);

  @Mapping(target = "user", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "updateAt", ignore = true)
  @Mapping(target = "title", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deletedBy", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  void updateEntityFromDto(UpdateCommentDto commentDto, @MappingTarget CommentEntity commentEntity);
}
