package vn.kms.ngaythobet.domain.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
@Constraint(validatedBy = ListUniqueValidator.class)
public @interface ListUnique {
    String message() default "{validation.list.unique.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
