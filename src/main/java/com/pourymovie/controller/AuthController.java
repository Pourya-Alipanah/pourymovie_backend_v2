package com.pourymovie.controller;

import com.pourymovie.dto.SignInDto;
import com.pourymovie.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {

  @Autowired
  AuthService authService;

  @PostMapping("/sign-in")
  public void signIn(@RequestParam SignInDto signInDto , HttpServletResponse response) throws Exception {
    authService.signIn(signInDto , response);
  }

}
