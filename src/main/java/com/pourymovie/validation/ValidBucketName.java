package com.pourymovie.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BucketNameValidator.class)
@Documented
public @interface ValidBucketName {
  String message() default "Invalid bucket name";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  // نوع enum رو از بیرون بگیریم
  Class<? extends Enum<?>> enumClass();
}