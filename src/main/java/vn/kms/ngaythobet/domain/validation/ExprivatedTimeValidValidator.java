// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.security.util.FieldUtils;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

public class ExprivatedTimeValidValidator implements ConstraintValidator<ExpritedTimeValid, Object> {

    private Class<? extends AuditableEntity> entity;
    private String firstField;
    private String secondField;
    private String thirdField;
    private String message;

    @PersistenceContext
    private EntityManager em;

    public void initialize(ExpritedTimeValid annotation) {
        this.entity = annotation.type();
        firstField = annotation.firstField();
        secondField = annotation.secondField();
        thirdField = annotation.thirdField();
        message = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isvalid;
        try {
            final Long firstValue = (Long) FieldUtils.getFieldValue(value, firstField);
            final LocalDateTime secondValue = (LocalDateTime) FieldUtils.getFieldValue(value, secondField);
            Query query = em
                    .createQuery("select " + thirdField + " from " + entity.getName() + " where id=" + firstValue);
            if (secondValue.isAfter((LocalDateTime) query.getSingleResult())) {
                isvalid = false;
            } else {
                isvalid = true;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not compare field value of " + firstField + " and " + secondField, ex);
        }
        if (!isvalid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(secondField).addConstraintViolation();
        }

        return isvalid;
    }
}
