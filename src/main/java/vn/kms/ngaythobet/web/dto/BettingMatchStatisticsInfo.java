package vn.kms.ngaythobet.web.dto;


import java.util.List;

import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.core.User;

public class BettingMatchStatisticsInfo {
    private List<BettingPlayer> bettingPlayersChooseCompetitor1;
    private double percentOfChoosingCompetitor1;
    private List<BettingPlayer> bettingPlayersChooseCompetitor2;
    private double percentOfChoosingCompetitor2;
    private List<User> totalBettingPlayers;
    private List<User> userNotBet;

    public List<BettingPlayer> getBettingPlayersChooseCompetitor1() {
        return bettingPlayersChooseCompetitor1;
    }

    public void setBettingPlayersChooseCompetitor1(List<BettingPlayer> bettingPlayersChooseCompetitor1) {
        this.bettingPlayersChooseCompetitor1 = bettingPlayersChooseCompetitor1;
    }

    public double getPercentOfChoosingCompetitor1() {
        return percentOfChoosingCompetitor1;
    }

    public void setPercentOfChoosingCompetitor1(double percentOfChoosingCompetitor1) {
        this.percentOfChoosingCompetitor1 = percentOfChoosingCompetitor1;
    }

    public double getPercentOfChoosingCompetitor2() {
        return percentOfChoosingCompetitor2;
    }

    public void setPercentOfChoosingCompetitor2(double percentOfChoosingCompetitor2) {
        this.percentOfChoosingCompetitor2 = percentOfChoosingCompetitor2;
    }

    public List<BettingPlayer> getBettingPlayersChooseCompetitor2() {
        return bettingPlayersChooseCompetitor2;
    }

    public void setBettingPlayersChooseCompetitor2(List<BettingPlayer> bettingPlayersChooseCompetitor2) {
        this.bettingPlayersChooseCompetitor2 = bettingPlayersChooseCompetitor2;
    }

    public List<User> getTotalBettingPlayers() {
        return totalBettingPlayers;
    }

    public void setTotalBettingPlayers(List<User> totalBettingPlayers) {
        this.totalBettingPlayers = totalBettingPlayers;
    }

    public List<User> getUserNotBet() {
        return userNotBet;
    }

    public void setUserNotBet(List<User> userNotBet) {
        this.userNotBet = userNotBet;
    }
}
