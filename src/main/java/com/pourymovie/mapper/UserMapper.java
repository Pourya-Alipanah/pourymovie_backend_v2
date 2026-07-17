package com.pourymovie.mapper;

import com.pourymovie.dto.request.SignUpDto;
import com.pourymovie.dto.request.UpdateUserDto;
import com.pourymovie.dto.response.UserDto;
import com.pourymovie.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  @Mappings({
    @Mapping(target = "password", ignore = true),
    @Mapping(target = "avatarUrl", ignore = true),
    @Mapping(target = "createdAt", ignore = true),
    @Mapping(target = "deletedAt", ignore = true),
    @Mapping(target = "hasSubscription", ignore = true),
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "resetPasswordToken", ignore = true),
    @Mapping(target = "resetPasswordTokenExpires", ignore = true),
    @Mapping(target = "role", ignore = true),
    @Mapping(target = "updateAt", ignore = true),
    @Mapping(target = "refreshToken", ignore = true)
  })
  UserEntity toEntity(SignUpDto user);

  UserDto toDto(UserEntity entity);

  default Page<UserDto> toDto(Page<UserEntity> entities) {
    return entities.map(this::toDto);
  }

  @Mappings({
    @Mapping(target = "password", ignore = true),
    @Mapping(target = "avatarUrl", ignore = true),
    @Mapping(target = "createdAt", ignore = true),
    @Mapping(target = "deletedAt", ignore = true),
    @Mapping(target = "hasSubscription", ignore = true),
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "resetPasswordToken", ignore = true),
    @Mapping(target = "resetPasswordTokenExpires", ignore = true),
    @Mapping(target = "role", ignore = true),
    @Mapping(target = "updateAt", ignore = true),
    @Mapping(target = "refreshToken", ignore = true)
  })
  void updateEntityFromDto(UpdateUserDto dto, @MappingTarget UserEntity entity);
}
