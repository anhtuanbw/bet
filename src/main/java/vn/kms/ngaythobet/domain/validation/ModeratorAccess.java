package vn.kms.ngaythobet.domain.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * @author thangpham
 *
 */

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ModeratorAccessValidator.class)
public @interface ModeratorAccess {
    String message() default "{validation.moderator.access.deny.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
