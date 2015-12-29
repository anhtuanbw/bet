// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Competitor;

@Entity
@Table(name = "betting_players")
public class BettingPlayer extends AuditableEntity {
    @DBRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "betting_match_id")
    private BettingMatch bettingMatch;

    @DBRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User player;

    @DBRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor betCompetitor;

    @Column
    private String comment;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
