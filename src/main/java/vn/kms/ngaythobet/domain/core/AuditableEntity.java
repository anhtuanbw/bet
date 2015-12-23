// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Map;

@MappedSuperclass
@EntityListeners(AuditableEntityListener.class)
public abstract class AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AuditIgnore
    private Long id;

    @Column
    @JsonIgnore
    @AuditIgnore
    private LocalDateTime createdAt;

    @Column
    @AuditIgnore
    @JsonIgnore
    private String createdBy;

    @Column
    @JsonIgnore
    @AuditIgnore
    private LocalDateTime modifiedAt;

    @Column
    @AuditIgnore
    @JsonIgnore
    private String modifiedBy;

    // only use by AuditableEntityListener to calculate changes
    @Transient
    @AuditIgnore
    @JsonIgnore
    private Map<String, Object> auditValuesByFields;

    public Long getId() {
        return id;
    }

    // only use by JPA or unit-test
    void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    Map<String, Object> getAuditValuesByFields() {
        return auditValuesByFields;
    }

    void setAuditValuesByFields(Map<String, Object> auditValuesByFields) {
        this.auditValuesByFields = auditValuesByFields;
    }
}
