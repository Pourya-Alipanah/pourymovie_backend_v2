package com.pourymovie.controller;

import com.pourymovie.exception.CustomException;
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
    throw new CustomException("Bad Reqdsfdsfuest" , HttpStatus.BAD_GATEWAY);
//    return "Hi from PouryMovie!";
  }
}
