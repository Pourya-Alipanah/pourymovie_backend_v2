package com.pourymovie.security.config;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;

  private final AppDefaults appDefaults;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationEntryPoint authenticationEntrypoint,
      AccessDeniedHandler accessDeniedHandler)
      throws Exception {

    http.cors(Customizer.withDefaults());

    http.csrf(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers(appDefaults.getPublicPaths())
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(authenticationEntrypoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .headers(
            headers ->
                headers
                    .contentSecurityPolicy(
                        csp ->
                            csp.policyDirectives(
                                "default-src 'self'; script-src 'self'; style-src 'self';"))
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                    .httpStrictTransportSecurity(
                        hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000)))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
