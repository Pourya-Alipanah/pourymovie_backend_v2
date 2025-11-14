package com.pourymovie.validation;

import com.pourymovie.dto.request.SignUpDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<ConfirmPasswordMatches, SignUpDto> {

  @Override
  public boolean isValid(SignUpDto dto, ConstraintValidatorContext context) {
    if (dto.password() == null || dto.confirmPassword() == null) {
      return false;
    }
    return dto.password().equals(dto.confirmPassword());
  }
}
