// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.MongoDbRef;

@Entity
@Table(name = "tournaments")
public class Tournament extends AuditableEntity {
    /**
     * 
     */

    @Column
    private String name;

    @Column
    private boolean activated;

    @Column(name = "number_of_competitors")
    private Long numOfCompetitors;

    @MongoDbRef
    @OneToMany(mappedBy = "tournament")
    @JsonIgnore
    private List<Competitor> competitors;

    @MongoDbRef
    @OneToMany(mappedBy = "tournament")
    @JsonIgnore
    private List<Group> groups;

    @MongoDbRef
    @OneToMany(mappedBy = "tournament")
    @JsonIgnore
    private List<Round> rounds;

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

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    public void setNumOfCompetitors(Long numOfCompetitors) {
        this.numOfCompetitors = numOfCompetitors;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

}
