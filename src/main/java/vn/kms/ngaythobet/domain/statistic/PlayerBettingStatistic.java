// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.statistic;

import java.time.LocalDateTime;

public class PlayerBettingStatistic {
    private String player;
    private String match;
    private LocalDateTime expiredBetTime;
    private int competitor1Score;
    private int competitor2Score;
    private double competitor1Balance;
    private double competitor2Balance;
    private String betCompetitorName;
    private long lossAmount;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public LocalDateTime getExpiredBetTime() {
        return expiredBetTime;
    }

    public void setExpiredBetTime(LocalDateTime expiredBetTime) {
        this.expiredBetTime = expiredBetTime;
    }

    public int getCompetitor1Score() {
        return competitor1Score;
    }

    public void setCompetitor1Score(int competitor1Score) {
        this.competitor1Score = competitor1Score;
    }

    public int getCompetitor2Score() {
        return competitor2Score;
    }

    public void setCompetitor2Score(int competitor2Score) {
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

    public long getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(long lossAmount) {
        this.lossAmount = lossAmount;
    }
}
