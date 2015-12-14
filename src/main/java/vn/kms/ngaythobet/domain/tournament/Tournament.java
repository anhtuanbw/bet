// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

import java.util.List;

public class Tournament extends AuditableEntity {
    private String name;
    private boolean active;

    public Tournament() {

    }

    public Tournament(String name) {
        this.name = name;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
