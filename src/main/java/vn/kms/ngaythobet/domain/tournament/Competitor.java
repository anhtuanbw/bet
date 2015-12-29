// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.MongoDbRef;

@Entity
@Table(name = "competitors")
public class Competitor extends AuditableEntity {
    @Column
    private String name;

    @MongoDbRef
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @MongoDbRef
    @JsonIgnore
    @ManyToMany(mappedBy = "competitors")
    private List<Round> rounds;

    public Competitor() {

    }

    public Competitor(Tournament tournament, String name) {
        this.tournament = tournament;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

}