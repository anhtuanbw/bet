// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;


import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateMatchInfo {
    @NotEmpty
    private String round;

    @NotEmpty
    private String competitor1;
    
    @NotEmpty
    private String competitor2;
    
    @NotEmpty
    private String time;
    
    @NotEmpty
    private String location;
    
    @NotEmpty
    @Size(max=512)
    private String comment;

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getCompetitor1() {
        return competitor1;
    }

    public void setCompetitor1(String competitor1) {
        this.competitor1 = competitor1;
    }

    public String getCompetitor2() {
        return competitor2;
    }

    public void setCompetitor2(String competitor2) {
        this.competitor2 = competitor2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
