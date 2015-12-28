package vn.kms.ngaythobet.web.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.validation.EntityExist;


public class CreateBettingMatchInfo {

    @NotNull
    private double balance1;

    @NotNull
    private double balance2;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime expiredTime;

    @NotNull
    @Size(min = 0 , message = "{validation.betAmount.min.message}")
    private double betAmount;

    @NotNull
    @EntityExist(type = Match.class, message = "{validation.existMatch.message}")
    private long matchId;

    @NotNull
    @EntityExist(type = Group.class, message = "{validation.existGroup.message}")
    private long groupId;

    @Size(min = 0, max = 512)
    private String comment;

    private boolean activated;

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

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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

}
