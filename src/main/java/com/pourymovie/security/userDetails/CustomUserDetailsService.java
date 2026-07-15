package com.pourymovie.security.userDetails;

import com.pourymovie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  @Lazy private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return new CustomUserDetails(userService.getUserByEmail(email));
  }
}
