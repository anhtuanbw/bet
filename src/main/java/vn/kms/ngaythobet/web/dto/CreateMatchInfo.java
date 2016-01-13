// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.validation.After;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.FieldNotMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldNotMatch(firstField = "competitor1", secondField = "competitor2", message = "{validation.matches.fieldNotMatch.message}")
public class CreateMatchInfo {
    private Long round;

    @NotNull(message = "{validation.matches.not-existCompetitorEntity.message}")
    @EntityExist(type = Competitor.class, message = "{validation.matches.not-existCompetitorEntity.message}")
    private Long competitor1;

    @NotNull(message = "{validation.matches.not-existCompetitorEntity.message}")
    @EntityExist(type = Competitor.class, message = "{validation.matches.not-existCompetitorEntity.message}")
    private Long competitor2;

    @After
    private LocalDateTime time;

    @NotEmpty
    private String location;

    @Size(max = 512)
    private String comment;

    public Long getRound() {
        return round;
    }

    public void setRound(Long round) {
        this.round = round;
    }

    public Long getCompetitor1() {
        return competitor1;
    }

    public void setCompetitor1(Long competitor1) {
        this.competitor1 = competitor1;
    }

    public Long getCompetitor2() {
        return competitor2;
    }

    public void setCompetitor2(Long competitor2) {
        this.competitor2 = competitor2;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
