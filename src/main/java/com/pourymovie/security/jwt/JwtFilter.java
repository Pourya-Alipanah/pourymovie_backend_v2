package com.pourymovie.security.jwt;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.enums.TokenNames;
import com.pourymovie.security.userDetails.CustomUserDetailsService;
import com.pourymovie.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private AppDefaults appDefaults;


  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    AntPathMatcher pathMatcher = new AntPathMatcher();
    return Arrays.stream(appDefaults.getPublicPaths())
            .anyMatch(pattern -> pathMatcher.match(pattern, path));
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    Optional<String> token = CookieUtils.getToken(TokenNames.ACCESS_TOKEN, request);


    if (token.isEmpty()) {
      filterChain.doFilter(request, response);
    }


      Long userId = jwtService.extractUserId(token.get());
      String userEmail = jwtService.extractEmail(token.get());

      var userDetails = userDetailsService.loadUserByUsername(userEmail);

      if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (jwtService.validateToken(token.get(), userDetails)) {
          var authToken = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }


    filterChain.doFilter(request, response);
  }
}
