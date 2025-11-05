package com.pourymovie.mapper;

import com.pourymovie.dto.SignUpDto;
import com.pourymovie.dto.UpdateUserDto;
import com.pourymovie.entity.UserEntity;
import org.mapstruct.*;

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
