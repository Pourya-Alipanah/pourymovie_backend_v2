package com.pourymovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PourymovieBackendV2Application {

  public static void main(String[] args) {
    SpringApplication.run(PourymovieBackendV2Application.class, args);
  }
}
