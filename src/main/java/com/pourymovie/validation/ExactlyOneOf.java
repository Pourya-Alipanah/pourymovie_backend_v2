package com.pourymovie.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExactlyOneOfValidator.class)
@Documented
public @interface ExactlyOneOf {
  String message() default "Exactly one field must be provided among this fields: " + "{fields}" ;
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String[] fields();
}
