package vn.kms.ngaythobet.domain.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

/**
 * 
 * @author thangpham
 *
 */
@Documented
@Constraint(validatedBy = EntityActivatedValidator.class)
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface EntityActivated {

    String message() default "{validation.entity.inactivated}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends AuditableEntity> type();
}
