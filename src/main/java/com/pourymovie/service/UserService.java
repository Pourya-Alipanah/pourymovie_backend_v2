package com.pourymovie.service;

import com.pourymovie.entity.UserEntity;
import com.pourymovie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow();
  }
}
