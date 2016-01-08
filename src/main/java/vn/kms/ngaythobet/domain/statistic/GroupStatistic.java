// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.statistic;

public class GroupStatistic {
    private String player;
    private int notlossCount;
    private int lossCount;
    private double lossAmount;
    private int notbetCount;
    private double notbetAmount;
    private double totalLossAmount;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getNotlossCount() {
        return notlossCount;
    }

    public void setNotlossCount(int notlossCount) {
        this.notlossCount = notlossCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    public double getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(double lossAmount) {
        this.lossAmount = lossAmount;
    }

    public int getNotbetCount() {
        return notbetCount;
    }

    public void setNotbetCount(int notbetCount) {
        this.notbetCount = notbetCount;
    }

    public double getNotbetAmount() {
        return notbetAmount;
    }

    public void setNotbetAmount(double notbetAmount) {
        this.notbetAmount = notbetAmount;
    }

    public double getTotalLossAmount() {
        return lossAmount + notbetAmount;
    }

    public void setTotalLossAmount(double totalLossAmount) {
        this.totalLossAmount = totalLossAmount;
    }
}
