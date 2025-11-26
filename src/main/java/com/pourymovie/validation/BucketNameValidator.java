package com.pourymovie.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class BucketNameValidator implements ConstraintValidator<ValidBucketName, String> {

  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(ValidBucketName constraintAnnotation) {
    this.enumClass = constraintAnnotation.enumClass();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) return false;

    return Arrays.stream(enumClass.getEnumConstants())
        .anyMatch(
            e -> {
              try {
                return e.getClass().getMethod("getValue").invoke(e).equals(value);
              } catch (Exception ex) {
                return false;
              }
            });
  }
}
