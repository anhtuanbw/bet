// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.statistic;

import java.time.LocalDateTime;

public class MatchStatistic {
    private String match;
    private LocalDateTime expiredBetTime;
    private double competitor1Balance;
    private double competitor2Balance;
    private int competitor1BetCount;
    private int competitor2BetCount;
    private int notbetCount;
    private long totalAmount;

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

    public int getCompetitor1BetCount() {
        return competitor1BetCount;
    }

    public void setCompetitor1BetCount(int competitor1BetCount) {
        this.competitor1BetCount = competitor1BetCount;
    }

    public int getCompetitor2BetCount() {
        return competitor2BetCount;
    }

    public void setCompetitor2BetCount(int competitor2BetCount) {
        this.competitor2BetCount = competitor2BetCount;
    }

    public int getNotbetCount() {
        return notbetCount;
    }

    public void setNotbetCount(int notbetCount) {
        this.notbetCount = notbetCount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
