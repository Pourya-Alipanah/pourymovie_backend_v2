package com.pourymovie.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class ExactlyOneOfValidator implements ConstraintValidator<ExactlyOneOf, Object> {
  private String[] fields;
  private String message;

  @Override
  public void initialize(ExactlyOneOf constraintAnnotation) {
    this.fields = constraintAnnotation.fields();
    this.message = constraintAnnotation.message().replace("{fields}", String.join(", ", fields));
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    int nonNullCount = 0;
    for (String fieldName : fields) {
      try {
        Field field = value.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.get(value) != null) {
          nonNullCount++;
        }
      } catch (Exception e) {
        return false;
      }
    }

    if (nonNullCount != 1) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      return false;
    }

    return true;
  }
}
