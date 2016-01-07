// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.statistic;

public class PlayerStatistic {
    private String player;
    private int notlossCount;
    private int lossCount;
    private long lossAmount;
    private int notbetCount;
    private long notbetAmount;
    private long totalLossAmount;

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

    public long getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(long lossAmount) {
        this.lossAmount = lossAmount;
    }

    public int getNotbetCount() {
        return notbetCount;
    }

    public void setNotbetCount(int notbetCount) {
        this.notbetCount = notbetCount;
    }

    public long getNotbetAmount() {
        return notbetAmount;
    }

    public void setNotbetAmount(long notbetAmount) {
        this.notbetAmount = notbetAmount;
    }

    public long getTotalLossAmount() {
        return lossAmount + notbetAmount;
    }

    public void setTotalLossAmount(long totalLossAmount) {
        this.totalLossAmount = totalLossAmount;
    }
}
