package vn.kms.ngaythobet.domain.statistic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.PlayerStatisticInfo;

@Service
@Transactional
public class PlayerStatisticService {

    private final BettingMatchRepository bettingMatchRepo;

    @Autowired
    public PlayerStatisticService(BettingMatchRepository bettingMatchRepo) {
        this.bettingMatchRepo = bettingMatchRepo;
    }

    public List<PlayerStatistic> playerStatistic(PlayerStatisticInfo playerStatisticInfo) {
        String username = SecurityUtil.getCurrentLogin();
        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        List<BettingMatch> bettingMatchs = bettingMatchRepo.findByGroupIdAndUsername(playerStatisticInfo.getGroupId(),
                username);
        for (BettingMatch bettingMatch : bettingMatchs) {
            // count lost amount when user bet
            if (!bettingMatch.getBettingPlayers().isEmpty()) {
                PlayerStatistic playerStatistic = new PlayerStatistic();
                Match match = bettingMatch.getMatch();
                playerStatistic.setCompetitor1Name(match.getCompetitor1().getName());
                playerStatistic.setCompetitor2Name(match.getCompetitor2().getName());
                playerStatistic.setExpiredBetTime(bettingMatch.getExpiredTime());
                playerStatistic.setCompetitor1Score(match.getScore1());
                playerStatistic.setCompetitor2Score(match.getScore2());
                playerStatistic.setCompetitor1Balance(bettingMatch.getBalance1().doubleValue());
                playerStatistic.setCompetitor2Balance(bettingMatch.getBalance2().doubleValue());
                Competitor betCompetitor = bettingMatch.getBettingPlayers().get(0).getBetCompetitor();
                playerStatistic.setBetCompetitorName(betCompetitor.getName());
                playerStatistic.setLossAmount(CalculateLostAmount(match, bettingMatch, betCompetitor));
                playerStatistics.add(playerStatistic);
            }
            // count lost amount when user didnot bet
            else {
                PlayerStatistic playerStatistic = new PlayerStatistic();
                Match match = bettingMatch.getMatch();
                playerStatistic.setCompetitor1Name(match.getCompetitor1().getName());
                playerStatistic.setCompetitor2Name(match.getCompetitor2().getName());
                playerStatistic.setExpiredBetTime(bettingMatch.getExpiredTime());
                playerStatistic.setCompetitor1Score(match.getScore1());
                playerStatistic.setCompetitor2Score(match.getScore2());
                playerStatistic.setCompetitor1Balance(bettingMatch.getBalance1().doubleValue());
                playerStatistic.setCompetitor2Balance(bettingMatch.getBalance2().doubleValue());
                playerStatistic.setBetCompetitorName("--");
                playerStatistic.setLossAmount(bettingMatch.getBetAmount().doubleValue());
                playerStatistics.add(playerStatistic);
            }
        }
        return playerStatistics;
    }

    private double CalculateLostAmount(Match match, BettingMatch bettingMatch, Competitor competitor) {
        double lostAmount = 0;
        double diff1 = match.getScore1().doubleValue() + bettingMatch.getBalance1().doubleValue();
        double diff2 = match.getScore2().doubleValue() + bettingMatch.getBalance2().doubleValue();
        double diff = 0;
        if (match.getCompetitor1().equals(competitor)) {
            diff = diff1 - diff2;
        } else {
            diff = diff2 - diff1;
        }
        if (diff > 0.25 || diff == 0.25) {
            lostAmount = 0;
        } else if (diff < 0.25 || diff > -0.5) {
            lostAmount = bettingMatch.getBetAmount().doubleValue() / 2;
        } else {
            lostAmount = bettingMatch.getBetAmount().doubleValue();
        }
        return lostAmount;
    }

}
