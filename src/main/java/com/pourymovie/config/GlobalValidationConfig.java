package com.pourymovie.config;

import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;


@ControllerAdvice
public class GlobalValidationConfig {

  private final Validator validator;

  public GlobalValidationConfig(Validator validator) {
    this.validator = validator;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }
}
