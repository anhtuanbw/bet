package vn.kms.ngaythobet.domain.statistic;

import java.util.List;

public class TotalPlayerStatistic {

    private List<PlayerStatistic> playerStatistics;
    private double totalLossAmount;

    public List<PlayerStatistic> getPlayerStatistics() {
        return playerStatistics;
    }

    public void setPlayerStatistics(List<PlayerStatistic> playerStatistics) {
        this.playerStatistics = playerStatistics;
    }

    public double getTotalLossAmount() {
        return totalLossAmount;
    }

    public void setTotalLossAmount(double totalLossAmount) {
        this.totalLossAmount = totalLossAmount;
    }
}
