package com.pourymovie.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class AuthenticationEntrypoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper;

  public AuthenticationEntrypoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "Authentication failed"
    );
    problemDetail.setTitle("Unauthorized");
    problemDetail.setProperty("path", request.getRequestURI());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/problem+json;charset=UTF-8");

    objectMapper.writeValue(response.getWriter(), problemDetail);
  }
}
