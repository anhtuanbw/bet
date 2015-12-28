// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.DELETE;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.INSERT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.domain.core.MongoDbRef;

@Component
public class AuditableEntityListener {
    private static final Logger logger = LoggerFactory.getLogger(AuditableEntityListener.class);

    private static ApplicationContext APP_CONTEXT;

    private final ApplicationContext appContext;

    // for JPA call
    public AuditableEntityListener() {
        this.appContext = null;
    }

    // for Spring container call
    @Autowired
    public AuditableEntityListener(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @PostConstruct
    public void initApplicationContext() {
        // cheat to set APP_CONTEXT since AuditableEntityListener is called by
        // JPA, not Spring container
        APP_CONTEXT = appContext;
    }

    @PrePersist
    public void prePersist(AuditableEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(SecurityUtil.getCurrentLogin());
    }

    @PreUpdate
    public void preUpdate(AuditableEntity entity) {
        entity.setModifiedAt(LocalDateTime.now());
        entity.setModifiedBy(SecurityUtil.getCurrentLogin());

        EntityManagerFactory factory = APP_CONTEXT.getBean(EntityManagerFactory.class);
        EntityManager em = factory.createEntityManager();
        AuditableEntity persistingEntity = em
                .createQuery("from " + entity.getClass().getName() + " where id = :id", entity.getClass())
                .setParameter("id", entity.getId()).getSingleResult();

        entity.setAuditValuesByFields(getAuditValuesByFields(persistingEntity));
    }

    @PostPersist
    public void postInsert(AuditableEntity entity) {
        saveChangeLog(new ChangeLog(INSERT, entity.getClass(), entity.getId()));
    }

    @PostUpdate
    public void postUpdate(AuditableEntity entity) {
        if (entity.getAuditValuesByFields() == null) {
            logger.warn("No old audit fields for entity {}, id={}", entity.getClass().getName(), entity.getId());
            return;
        }

        Map<String, Change> changes = getChanges(entity.getAuditValuesByFields(), getAuditValuesByFields(entity));
        if (changes.isEmpty()) {
            logger.warn("No change for entity {}, id={}", entity.getClass().getName(), entity.getId());
            return;
        }

        saveChangeLog(new ChangeLog(entity.getClass(), entity.getId(), changes));
    }

    @PostRemove
    public void postRemove(AuditableEntity entity) {
        saveChangeLog(new ChangeLog(DELETE, entity.getClass(), entity.getId()));
    }

    private void saveChangeLog(ChangeLog log) {
        APP_CONTEXT.getBean(ChangeLogRepository.class).save(log);
        // TODO: fire action for websocket processing
    }

    private static Map<String, Object> getAuditValuesByFields(AuditableEntity entity) {
        Map<String, Object> auditValuesByFields = new HashMap<>();
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MongoDbRef.class)) {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    List<Long> entityIds = new ArrayList<>();
                    ((List<AuditableEntity>) field.get(entity)).forEach(referenceEntity -> {
                        entityIds.add(referenceEntity.getId());
                    });
                    auditValuesByFields.put(field.getName(), entityIds);
                } else {
                    AuditableEntity referenceEntity = (AuditableEntity) field.get(entity);
                    auditValuesByFields.put(field.getName(), referenceEntity.getId());
                }
            } else {
                auditValuesByFields.put(field.getName(), field.get(entity));
            }
        } , field -> !field.isAnnotationPresent(AuditIgnore.class));

        return auditValuesByFields;
    }

    private static Map<String, Change> getChanges(Map<String, Object> oldValues, Map<String, Object> newValues) {
        Map<String, Change> changes = new HashMap<>();

        newValues.entrySet().forEach(entry -> {
            Object oldValue = oldValues.get(entry.getKey());

            if ((oldValue == null && entry.getValue() != null)
                    || (oldValue != null && !oldValue.equals(entry.getValue()))) {
                changes.put(entry.getKey(), new Change(oldValue, entry.getValue()));
            }
        });

        return changes;
    }
}
