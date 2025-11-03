package com.pourymovie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/hi")
  public String sayHi() {
    System.out.println("sdfsdfdsfsdgeth");
    throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "This is a test exception from PouryMovie!");
//    return "Hi from PouryMovie!";
  }
}
