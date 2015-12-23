// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;


import java.time.LocalDateTime;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class CreateMatchInfo {
    @NotEmpty
    private long round;

    @NotEmpty
    private Long competitor1;
    
    @NotEmpty
    private Long competitor2;
    
    @NotEmpty
    private LocalDateTime time;
    
    @NotEmpty
    private String location;
    
    @NotEmpty
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
