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
    private Long numOfCompetitor;

    @OneToMany(mappedBy = "tournament")
    private List<Competitor> competitors;

    @OneToMany(mappedBy = "tournament")
    private List<Group> groups;

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

    public Long getNumOfCompetitor() {
        return numOfCompetitor;
    }

    public void setNumOfCompetitor(Long numOfCompetitor) {
        this.numOfCompetitor = numOfCompetitor;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
