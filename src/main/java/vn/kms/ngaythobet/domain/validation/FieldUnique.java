// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FieldUniqueValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface FieldUnique {

    String message() default "{validation.fieldUnique.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    Class<? extends AuditableEntity> entity();
}
