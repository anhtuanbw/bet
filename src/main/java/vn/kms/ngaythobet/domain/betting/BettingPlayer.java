// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.MongoDbRef;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Competitor;

@Entity
@Table(name = "betting_players")
public class BettingPlayer extends AuditableEntity {
    @MongoDbRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "betting_match_id")
    private BettingMatch bettingMatch;

    @MongoDbRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User player;

    @MongoDbRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor betCompetitor;

    public BettingMatch getBettingMatch() {
        return bettingMatch;
    }

    public void setBettingMatch(BettingMatch bettingMatch) {
        this.bettingMatch = bettingMatch;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Competitor getBetCompetitor() {
        return betCompetitor;
    }

    public void setBetCompetitor(Competitor betCompetitor) {
        this.betCompetitor = betCompetitor;
    }

}
