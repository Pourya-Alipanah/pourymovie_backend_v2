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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final UploadCenterService uploadCenterService;
  @Lazy private final UserService self;

  @Cacheable(value = "users:email", key = "#email")
  @Transactional(readOnly = true)
  public UserEntity getUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with email " + email + " not found"));
  }

  public Optional<UserEntity> getOptionalUserByEmail(String email) {
    return userRepository.findByEmail(email);
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

  public UserEntity createUserForOAuth2(OAuth2User oAuth2User, UserRole userRole) {
    UserEntity user = new UserEntity();
    String firstName = oAuth2User.getAttribute("given_name");
    String lastName = oAuth2User.getAttribute("family_name");
    String email = oAuth2User.getAttribute("email");
    String picture = oAuth2User.getAttribute("picture");

    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setAvatarUrl(picture);
    user.setRole(userRole);
    return userRepository.save(user);
  }

  public Page<UserEntity> getUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public UserEntity getCurrentUser() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return self.getUserByEmail(userEmail);
  }

  @Cacheable(value = "users:id", key = "#id")
  @Transactional(readOnly = true)
  public UserEntity getUserById(Long id) {
    return userRepository.findById(id).orElseThrow();
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "users:email", key = "#email"),
        @CacheEvict(value = "users:id", key = "#result.id")
      })
  public UserEntity updateUserByEmail(String email, UpdateUserDto updateUserDto) throws Exception {
    UserEntity existingUser = userRepository.findByEmail(email).orElseThrow();
    userMapper.updateEntityFromDto(updateUserDto, existingUser);

    if (updateUserDto.avatarUrl() != null) {
      var avatarUrl =
          uploadCenterService.confirmUpload(
              updateUserDto.avatarUrl().key(), UploadFromEntity.USER, UploadType.AVATAR);
      existingUser.setAvatarUrl(avatarUrl);
    }
    return userRepository.save(existingUser);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "users:id", key = "#id"),
        @CacheEvict(value = "users:email", allEntries = true)
      })
  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }

  public void deleteCurrentUser() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity user = self.getUserByEmail(userEmail);
    self.deleteUserById(user.getId());
  }

  public UserEntity updateCurrentUser(UpdateUserDto userDto) throws Exception {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return self.updateUserByEmail(userEmail, userDto);
  }

  public UserEntity updateUserById(Long id, @Valid UpdateUserDto updateUserDto) throws Exception {
    UserEntity existingUser = self.getUserById(id);
    return self.updateUserByEmail(existingUser.getEmail(), updateUserDto);
  }
}
