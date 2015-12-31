package vn.kms.ngaythobet.domain.validation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

public class EntityActivatedValidator implements ConstraintValidator<EntityActivated, Object> {

    private Class<? extends AuditableEntity> entity;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void initialize(EntityActivated entityActivated) {
        this.entity = entityActivated.type();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext arg1) {
        String queryString = String.format("select id from %s where id= :entityId and activated = true",
                entity.getName());
        return !em.createQuery(queryString).setParameter("entityId", (Long) value).getResultList().isEmpty();
    }

}
