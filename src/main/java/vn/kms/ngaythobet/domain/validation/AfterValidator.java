// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AfterValidator implements
        ConstraintValidator<After, LocalDateTime> {

    @Override
    public void initialize(After constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDateTime time,
            ConstraintValidatorContext context) {

        return time.isBefore(LocalDateTime.now()) ? false : true;
    }
}
