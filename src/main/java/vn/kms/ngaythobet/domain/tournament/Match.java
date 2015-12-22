// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

@Entity
@Table(name = "matches")
public class Match extends AuditableEntity {

    @Column(name = "score_1")
    private Long score1;

    @Column(name = "score_2")
    private Long score2;

    @Column(name = "match_time")
    private LocalDateTime matchTime;
    
    @Column
    private String comment;

    @Column
    private String location;

    @ManyToOne
    @JoinColumn(name = "competitor_id_1")
    private Competitor competitor1;

    @ManyToOne
    @JoinColumn(name = "competitor_id_2")
    private Competitor competitor2;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    public Long getScore1() {
        return score1;
    }

    public void setScore1(Long score1) {
        this.score1 = score1;
    }

    public Long getScore2() {
        return score2;
    }

    public void setScore2(Long score2) {
        this.score2 = score2;
    }

    public LocalDateTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Competitor getCompetitor1() {
        return competitor1;
    }

    public void setCompetitor1(Competitor competitor1) {
        this.competitor1 = competitor1;
    }

    public Competitor getCompetitor2() {
        return competitor2;
    }

    public void setCompetitor2(Competitor competitor2) {
        this.competitor2 = competitor2;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }
}