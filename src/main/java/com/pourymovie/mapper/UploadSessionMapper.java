package com.pourymovie.mapper;

import com.pourymovie.dto.request.InitiateChunkUploadDto;
import com.pourymovie.dto.response.ChunkUploadDto;
import com.pourymovie.entity.UploadSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UploadSessionMapper {
  @Mapping(target = "expireAt", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "uploadId", ignore = true)
  @Mapping(target = "sessionId", ignore = true)
  @Mapping(target = "uploadedParts", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  UploadSessionEntity toEntity(InitiateChunkUploadDto initiateChunkUploadDto);

  ChunkUploadDto toDto(UploadSessionEntity uploadSessionEntity);
}
