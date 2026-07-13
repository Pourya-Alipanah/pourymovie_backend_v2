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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final UploadCenterService uploadCenterService;

  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow();
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
