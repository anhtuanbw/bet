// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends AuditableEntity {
    public enum Role {
        ADMIN,
        USER,
        ANONYMOUS;

        public String getAuthority() {
            return "ROLE_" + name();
        }
    }
    
    @Column
    private String username;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String languageTag;

    @Enumerated(EnumType.STRING)
    @Column
    @JsonIgnore
    private Role role = Role.USER;

    @Column
    @JsonIgnore
    private boolean activated;

    @Column
    @JsonIgnore
    private String activationKey;

    @Column
    @JsonIgnore
    private String resetKey;

    @Column
    @JsonIgnore
    private LocalDateTime resetTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public LocalDateTime getResetTime() {
        return resetTime;
    }

    public void setResetTime(LocalDateTime resetTime) {
        this.resetTime = resetTime;
    }
}
