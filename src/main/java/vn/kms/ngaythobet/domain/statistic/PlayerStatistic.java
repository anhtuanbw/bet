// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.statistic;

import java.time.LocalDateTime;

public class PlayerStatistic {
    private String player;
    private String competitor1Name;
    private String competitor2Name;
    private LocalDateTime expiredBetTime;
    private long competitor1Score;
    private long competitor2Score;
    private double competitor1Balance;
    private double competitor2Balance;
    private String betCompetitorName;
    private double lossAmount;
    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }
    public String getCompetitor1Name() {
        return competitor1Name;
    }
    public void setCompetitor1Name(String competitor1Name) {
        this.competitor1Name = competitor1Name;
    }
    public String getCompetitor2Name() {
        return competitor2Name;
    }
    public void setCompetitor2Name(String competitor2Name) {
        this.competitor2Name = competitor2Name;
    }
    public LocalDateTime getExpiredBetTime() {
        return expiredBetTime;
    }
    public void setExpiredBetTime(LocalDateTime expiredBetTime) {
        this.expiredBetTime = expiredBetTime;
    }
    public long getCompetitor1Score() {
        return competitor1Score;
    }
    public void setCompetitor1Score(long competitor1Score) {
        this.competitor1Score = competitor1Score;
    }
    public long getCompetitor2Score() {
        return competitor2Score;
    }
    public void setCompetitor2Score(long competitor2Score) {
        this.competitor2Score = competitor2Score;
    }
    public double getCompetitor1Balance() {
        return competitor1Balance;
    }
    public void setCompetitor1Balance(double competitor1Balance) {
        this.competitor1Balance = competitor1Balance;
    }
    public double getCompetitor2Balance() {
        return competitor2Balance;
    }
    public void setCompetitor2Balance(double competitor2Balance) {
        this.competitor2Balance = competitor2Balance;
    }
    public String getBetCompetitorName() {
        return betCompetitorName;
    }
    public void setBetCompetitorName(String betCompetitorName) {
        this.betCompetitorName = betCompetitorName;
    }
    public double getLossAmount() {
        return lossAmount;
    }
    public void setLossAmount(double lossAmount) {
        this.lossAmount = lossAmount;
    }
    
    
}
