package com.pourymovie.service;

import com.pourymovie.dto.request.SignUpDto;
import com.pourymovie.dto.request.UpdateUserDto;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.UploadFromEntity;
import com.pourymovie.enums.UploadType;
import com.pourymovie.enums.UserRole;
import com.pourymovie.mapper.UserMapper;
import com.pourymovie.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserMapper userMapper;
  @Autowired private UploadCenterService uploadCenterService;

  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow();
  }

  public UserEntity createUser(SignUpDto user, UserRole role) throws Exception {
    UserEntity mappedUser = userMapper.toEntity(user);
    if (user.avatarUrl() != null) {
      var avatarUrl =
          uploadCenterService.confirmUpload(
              user.avatarUrl().key(), UploadFromEntity.USER, UploadType.AVATAR);
      mappedUser.setAvatarUrl(avatarUrl);
    }
    mappedUser.setPassword(passwordEncoder.encode(user.password()));
    mappedUser.setRole(role);
    return userRepository.save(mappedUser);
  }

  public Page<UserEntity> getUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public UserEntity getCurrentUser() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return getUserByEmail(userEmail);
  }

  public UserEntity getUserById(Long id) {
    return userRepository.findById(id).orElseThrow();
  }

  public UserEntity updateUserByEmail(String email, UpdateUserDto updateUserDto) throws Exception {
    UserEntity existingUser = getUserByEmail(email);
    userMapper.updateEntityFromDto(updateUserDto, existingUser);

    if (updateUserDto.avatarUrl() != null) {
      var avatarUrl =
          uploadCenterService.confirmUpload(
              updateUserDto.avatarUrl().key(), UploadFromEntity.USER, UploadType.AVATAR);
      existingUser.setAvatarUrl(avatarUrl);
    }
    return userRepository.save(existingUser);
  }

  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }

  public void deleteCurrentUser() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity user = getUserByEmail(userEmail);
    userRepository.delete(user);
  }

  public UserEntity updateCurrentUser(UpdateUserDto userDto) throws Exception {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return updateUserByEmail(userEmail, userDto);
  }

  public UserEntity updateUserById(Long id, @Valid UpdateUserDto updateUserDto) throws Exception {
    UserEntity existingUser = getUserById(id);
    return updateUserByEmail(existingUser.getEmail(), updateUserDto);
  }
}
