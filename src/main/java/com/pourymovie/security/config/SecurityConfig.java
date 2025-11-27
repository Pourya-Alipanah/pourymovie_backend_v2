package com.pourymovie.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pourymovie.config.AppDefaults;
import com.pourymovie.exception.AccessDeniedHandlerImpl;
import com.pourymovie.exception.AuthenticationEntrypoint;
import com.pourymovie.security.jwt.JwtFilter;
import com.pourymovie.security.userDetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtFilter jwtFilter;

  @Autowired
  private AppDefaults appDefaults;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntrypoint(ObjectMapper objectMapper) {
    return new AuthenticationEntrypoint(objectMapper);
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
    return new AccessDeniedHandlerImpl(objectMapper);
  }

  @Bean
  public DaoAuthenticationProvider authenticationManager(PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
    var authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider,
                                                 AuthenticationEntryPoint authenticationEntrypoint, AccessDeniedHandler accessDeniedHandler) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> requests
                    .requestMatchers(appDefaults.getPublicPaths())
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authenticationEntrypoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
