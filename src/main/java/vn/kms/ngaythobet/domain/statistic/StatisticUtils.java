package vn.kms.ngaythobet.domain.statistic;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Match;

public class StatisticUtils {

    public double calculateLossAmount(BettingMatch bettingMatch, Competitor competitor) {
        double lostAmount = 0;
        Match match = bettingMatch.getMatch();
        double diff1 = match.getScore1().doubleValue() + bettingMatch.getBalance1().doubleValue();
        double diff2 = match.getScore2().doubleValue() + bettingMatch.getBalance2().doubleValue();
        double diff = 0;
        if (match.getCompetitor1().equals(competitor)) {
            diff = diff1 - diff2;
        } else if (match.getCompetitor2().equals(competitor)) {
            diff = diff2 - diff1;
        } else {
            diff = -1;
        }
        if (diff > 0.25 || diff == 0.25) {
            lostAmount = 0;
        } else if (diff < 0.25 && diff > -0.5) {
            lostAmount = bettingMatch.getBetAmount().doubleValue() / 2;
        } else {
            lostAmount = bettingMatch.getBetAmount().doubleValue();
        }
        return lostAmount;
    }
}
