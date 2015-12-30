// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ListEntityExistValidator implements ConstraintValidator<ListEntityExist, List<Long>> {

    private Class<? extends AuditableEntity> entity;

    @PersistenceContext
    private EntityManager em;

    public void initialize(ListEntityExist annotation) {
        this.entity = annotation.type();
    }

    @Override
    public boolean isValid(List<Long> entityIds, ConstraintValidatorContext context) {
        for (Long entityId : entityIds) {
            Query query = em.createQuery("select id from " + entity.getName() + " where id=" + entityId);
            if(query.getResultList().isEmpty()){
                return false;
            }
        }
        return true;
    }
}
