// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.security.util.FieldUtils;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.tournament.Competitor;

public class ListEntityJoinedValidValidator implements ConstraintValidator<ListEntityJoinedValid, Object> {

    private Class<? extends AuditableEntity> entity;
    private String entityId;
    private String fieldName;
    private String entities;
    private String message;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void initialize(ListEntityJoinedValid annotation) {
        this.entity = annotation.type();
        entityId = annotation.entityId();
        fieldName = annotation.fieldName();
        entities = annotation.entities();
        message = annotation.message();

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Long entityIdValue = (Long) FieldUtils.getFieldValue(value, entityId);
            final List<Long> entitiesValue = (List<Long>) FieldUtils.getFieldValue(value, entities);
            Query query = em.createQuery("select entity." + fieldName + " from " + entity.getName()
                    + " entity where entity.id = " + entityIdValue);
            Object result = query.getResultList();
            List<Long> competitorIds = new ArrayList<>();
            for (Object object : (List<Object>) result) {
                if (object instanceof Competitor) {
                    competitorIds.add(((Competitor) object).getId());
                }
            }
            if (!competitorIds.containsAll(entitiesValue)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(entities)
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not compare field value of " + entityId + " and " + fieldName, ex);
        }
        return true;
    }
}
