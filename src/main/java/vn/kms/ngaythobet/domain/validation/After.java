// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AfterValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface After {

    String message() default "{validation.matchService.updateScore.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}