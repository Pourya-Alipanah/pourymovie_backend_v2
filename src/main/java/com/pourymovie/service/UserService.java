package com.pourymovie.service;

import com.pourymovie.dto.SignUpDto;
import com.pourymovie.entity.UserEntity;
import com.pourymovie.enums.UserRole;
import com.pourymovie.mapper.UserMapper;
import com.pourymovie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}
