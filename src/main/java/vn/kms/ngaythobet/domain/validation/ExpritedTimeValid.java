// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ExprivatedTimeValidValidator.class)
@Documented
public @interface ExpritedTimeValid {

    String message() default "{validation.expritedTime.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    Class<? extends AuditableEntity> type();

    String firstField();

    String secondField();
    
    String thirdField();
}