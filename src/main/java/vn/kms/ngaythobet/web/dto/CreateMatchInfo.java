// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;


import java.time.LocalDateTime;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.validation.EntityExist;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateMatchInfo {
    @NotNull
    private Long round;

    @NotNull
    @EntityExist(type = Competitor.class)
    private Long competitor1;
    
    @NotNull
    @EntityExist(type = Competitor.class)
    private Long competitor2;
    
    @NotNull
    private LocalDateTime time;
    
    @NotEmpty
    private String location;
    
    @Size(max=512)
    private String comment;

    public long getRound() {
        return round;
    }

    public void setRound(long round) {
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
