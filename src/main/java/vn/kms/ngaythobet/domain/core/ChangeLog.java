// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.kms.ngaythobet.domain.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "changelogs")
public class ChangeLog {
    public enum Action {
        INSERT,
        UPDATE,
        DELETE
    }

    @Id
    private String id;

    @Indexed
    private String username;

    @Indexed
    private LocalDateTime timestamp;

    @Indexed
    private Action action;

    @Indexed
    private String entityType;

    @Indexed
    private long entityId;

    private Map<String, Change> entityChanges;

    // Spring Data MongoDB call
    public ChangeLog() {
    }

    public ChangeLog(Action action, Class<? extends AuditableEntity> entityType, long entityId) {
        this.username = SecurityUtil.getCurrentLogin();
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.entityType = entityType.getName();
        this.entityId = entityId;
    }

    public ChangeLog(Class<? extends AuditableEntity> entityType, long entityId, Map<String, Change> entityChanges) {
        this(Action.UPDATE, entityType, entityId);
        this.entityChanges = entityChanges;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public Map<String, Change> getEntityChanges() {
        return entityChanges;
    }

    public void setEntityChanges(Map<String, Change> entityChanges) {
        this.entityChanges = entityChanges;
    }

    public static class Change {
        private final Object oldValue;
        private final Object newValue;

        public Change(Object oldValue, Object newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Object getOldValue() {
            return oldValue;
        }

        public Object getNewValue() {
            return newValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Change change = (Change) o;

            if (oldValue != null ? !oldValue.equals(change.oldValue) : change.oldValue != null) return false;

            return !(newValue != null ? !newValue.equals(change.newValue) : change.newValue != null);
        }

        @Override
        public int hashCode() {
            int result = oldValue != null ? oldValue.hashCode() : 0;
            result = 31 * result + (newValue != null ? newValue.hashCode() : 0);

            return result;
        }

        @Override
        public String toString() {
            return String.format("{old: %s, new: %s}", oldValue, newValue);
        }
    }
}
