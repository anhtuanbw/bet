// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import org.springframework.util.StringUtils;
import vn.kms.ngaythobet.domain.core.AuditableEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldUniqueValidator implements ConstraintValidator<FieldUnique, Object> {

    private Class<? extends AuditableEntity> entity;

    private String fieldName;

    @PersistenceContext
    private EntityManager em;

    public void initialize(FieldUnique annotation) {
        entity = annotation.entity();
        fieldName = annotation.field();
        if (StringUtils.isEmpty(fieldName)) {
            fieldName = "";
        }
    }

    @Override
    public boolean isValid(Object fieldValue, ConstraintValidatorContext context) {
        String queryString = String.format("select id from %s where %s = :fieldValue", entity.getName(), fieldName);

        return em
            .createQuery(queryString)
            .setParameter("fieldValue", fieldValue)
            .getResultList().isEmpty();
    }
}
