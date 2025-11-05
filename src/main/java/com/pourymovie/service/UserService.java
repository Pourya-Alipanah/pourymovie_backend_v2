package com.pourymovie.service;

import com.pourymovie.dto.SignUpDto;
import com.pourymovie.dto.UpdateUserDto;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.UserRole;
import com.pourymovie.mapper.UserMapper;
import com.pourymovie.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserMapper userMapper;

  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow();
  }

  public UserEntity createUser(SignUpDto user , UserRole role) {
    UserEntity mappedUser = userMapper.toEntity(user);
    mappedUser.setPassword(passwordEncoder.encode(user.getPassword()));
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

  public UserEntity updateUserByEmail(String email, UpdateUserDto updateUserDto) {
    UserEntity existingUser = getUserByEmail(email);
    userMapper.updateEntityFromDto(updateUserDto, existingUser);
    return userRepository.save(existingUser);
  }

  public void deleteUserById(Long id) {
    userRepository.deleteById(id);
  }

  public void deleteCurrentUser(){
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity user = getUserByEmail(userEmail);
    userRepository.delete(user);
  }

  public UserEntity updateCurrentUser(UpdateUserDto userDto) {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return updateUserByEmail(userEmail, userDto);
  }

  public UserEntity updateUserById(Long id, @Valid UpdateUserDto updateUserDto) {
    UserEntity existingUser = getUserById(id);
    userMapper.updateEntityFromDto(updateUserDto, existingUser);
    return userRepository.save(existingUser);
  }
}
