// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

@Entity
@Table(name = "rounds")
public class Round extends AuditableEntity {

    @Column
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(mappedBy = "round")
    private List<Match> matches;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "round_competitor", joinColumns = { @JoinColumn(name = "round_id") }, inverseJoinColumns = {
            @JoinColumn(name = "competitor_id") })
    private List<Competitor> competitors;

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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }
}