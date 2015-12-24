// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

@Entity
@Table(name = "tournaments")
public class Tournament extends AuditableEntity {
    @Column
    private String name;

    @Column
    private boolean activated;

    @Column(name = "number_of_competitors")
    private Long numOfCompetitors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Long getNumOfCompetitors() {
        return numOfCompetitors;
    }

    public void setNumOfCompetitor(Long numOfCompetitors) {
        this.numOfCompetitors = numOfCompetitors;
    }

}
