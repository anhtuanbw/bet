// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.MongoDbRef;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "betting_matches")
public class BettingMatch extends AuditableEntity {
    
    @ManyToOne
    @MongoDbRef
    @JsonIgnore
    @JoinColumn(name = "group_id")
    private Group group;
    
    @ManyToOne 
    @JoinColumn(name = "match_id")
    @MongoDbRef
    @JsonIgnore
    private Match match;
    
    @Column(name="balance1")
    private double balance1;
    
    @Column(name = "balance2")
    private double balance2;
    
    @Column(name = "expire_time")
    private LocalDateTime expiredTime;
    
    @Column (name = "bet_amount")
    private double betAmount;
    
    @Column (name = "comment")
    private String comment;
    
    @Column (name = "activated")
    private boolean activated;
    
    @ManyToMany
    @MongoDbRef
    @JsonIgnore
    @JoinTable(name = "betting_match_details", joinColumns = { @JoinColumn(name = "betting_match_id") }, inverseJoinColumns = {
            @JoinColumn(name = "betting_player_id") })
    private List<BettingPlayer> bettingPlayers;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public double getBalance1() {
        return balance1;
    }

    public void setBalance1(double balance1) {
        this.balance1 = balance1;
    }

    public double getBalance2() {
        return balance2;
    }

    public void setBalance2(double balance2) {
        this.balance2 = balance2;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<BettingPlayer> getBettingPlayers() {
        return bettingPlayers;
    }

    public void setBettingPlayers(List<BettingPlayer> bettingPlayers) {
        this.bettingPlayers = bettingPlayers;
    }
    
    
}
