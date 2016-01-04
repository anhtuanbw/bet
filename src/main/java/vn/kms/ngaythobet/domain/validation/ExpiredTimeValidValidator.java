// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.security.util.FieldUtils;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

public class ExpiredTimeValidValidator implements ConstraintValidator<ExpiredTimeValid, Object> {

    private Class<? extends AuditableEntity> entity;
    private String entityId;
    private String targetField;
    private String fieldName;
    private String message;

    @PersistenceContext
    private EntityManager em;

    public void initialize(ExpiredTimeValid annotation) {
        this.entity = annotation.type();
        entityId = annotation.entityId();
        targetField = annotation.targetField();
        fieldName = annotation.fieldName();
        message = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Long entityIdValue = (Long) FieldUtils.getFieldValue(value, entityId);
            final LocalDateTime targetFieldValue = (LocalDateTime) FieldUtils.getFieldValue(value, targetField);
            Query query = em
                    .createQuery("select " + fieldName + " from " + entity.getName() + " where id=" + entityIdValue);
            List<Object> resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                if (!targetFieldValue.isBefore((LocalDateTime) resultList.get(0))) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message).addPropertyNode(targetField)
                            .addConstraintViolation();
                    return false;
                }
            } 
        } catch (Exception ex) {
            throw new RuntimeException("Could not compare field value of " + entityId + " and " + targetField, ex);
        }

        return true;
    }
}
