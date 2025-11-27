package com.pourymovie.controller;

import com.pourymovie.dto.request.SignInDto;
import com.pourymovie.dto.request.SignUpDto;
import com.pourymovie.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
@RequestMapping("/auth")
public class AuthController {

  @Autowired AuthService authService;

  @ApiResponse(responseCode = "204", description = "Successful Operation And Set Cookies")
  @PostMapping("/sign-in")
  public void signIn(@Valid @RequestBody SignInDto signInDto, HttpServletResponse response)
      throws Exception {
    authService.signIn(signInDto, response);
  }

  @ApiResponse(responseCode = "204", description = "Successful Operation And Set Cookies")
  @PostMapping("/sign-up")
  public void signUp(@Valid @RequestBody SignUpDto signInDto, HttpServletResponse response)
      throws Exception {
    authService.signUp(signInDto, response);
  }

  @ApiResponse(responseCode = "204", description = "Successful Operation And Set Cookies")
  @GetMapping("/refresh-tokens")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    authService.refreshToken(request, response);
  }

  @ApiResponse(responseCode = "204", description = "Successful Operation And Remove Cookies")
  @PostMapping("/sign-out")
  public void signOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
    authService.signOut(request, response);
  }
}
