package com.pourymovie.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public AccessDeniedHandlerImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN,
            "Access denied"
    );
    problemDetail.setTitle("Forbidden");

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/problem+json;charset=UTF-8");

    objectMapper.writeValue(response.getWriter(), problemDetail);
  }
}
