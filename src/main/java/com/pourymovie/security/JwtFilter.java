package com.pourymovie.security;

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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CustomUserDetailsService userDetailsService;


  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    String token = null;
    if (request.getCookies() != null) {
      for (var cookie : request.getCookies()) {
        if ("accessToken".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }


    if (token == null || token.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {

      Long userId = jwtService.extractUserId(token);
      String userEmail = jwtService.extractEmail(token);
      if (userEmail == null || userEmail.isBlank()) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid token payload");
        return;
      }
      var userDetails = userDetailsService.loadUserByUsername(userEmail);

      if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (jwtService.validateToken(token, userDetails)) {
          var authToken = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid or expired token");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
