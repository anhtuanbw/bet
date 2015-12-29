// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.MongoDbRef;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;

@Entity
@Table(name = "betting_matches")
public class BettingMatch extends AuditableEntity {
    @MongoDbRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @MongoDbRef
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @MongoDbRef
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bettingMatch")
    private List<BettingPlayer> bettingPlayers;

    @Column
    private BigDecimal balance1;

    @Column
    private BigDecimal balance2;

    @Column(name = "expire_time")
    private LocalDateTime expiredTime;

    @Column(name = "bet_amount")
    private BigDecimal betAmount;

    @Column
    private String comment;

    @Column
    private String description;

    @Column
    private boolean activated;

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

    public List<BettingPlayer> getBettingPlayers() {
        return bettingPlayers;
    }

    public void setBettingPlayers(List<BettingPlayer> bettingPlayers) {
        this.bettingPlayers = bettingPlayers;
    }

    public BigDecimal getBalance1() {
        return balance1;
    }

    public void setBalance1(BigDecimal balance1) {
        this.balance1 = balance1;
    }

    public BigDecimal getBalance2() {
        return balance2;
    }

    public void setBalance2(BigDecimal balance2) {
        this.balance2 = balance2;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}