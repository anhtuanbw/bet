// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import org.springframework.security.util.FieldUtils;

import vn.kms.ngaythobet.domain.util.DataInvalidException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldNotMatchValidator implements
        ConstraintValidator<FieldNotMatch, Object> {

    private String firstField;
    private String secondField;
    private String message;

    @Override
    public void initialize(FieldNotMatch constraintAnnotation) {
        firstField = constraintAnnotation.firstField();
        secondField = constraintAnnotation.secondField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value,
            final ConstraintValidatorContext context) {
        try {
            final Object firstValue = FieldUtils.getFieldValue(value,
                    firstField);
            final Object secondValue = FieldUtils.getFieldValue(value,
                    secondField);

            if (firstValue == null && secondValue == null) {
                return false;
            }

            if (firstValue == null || secondValue == null) {
                throw new DataInvalidException(
                        "exception.matchService.createMatch.number-of-competitor");
            }

            boolean matched = firstValue.equals(secondValue);
            if (matched) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(secondField).addConstraintViolation();
            }

            return !matched;
        } catch (Exception ex) {
            return false;
        }
    }
}
