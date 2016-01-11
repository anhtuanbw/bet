package vn.kms.ngaythobet.domain.statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.PlayerStatisticInfo;

@Service
@Transactional
public class PlayerStatisticService {

    private final BettingMatchRepository bettingMatchRepo;

    private final BettingPlayerRepository bettingPlayerRepo;

    @Autowired
    public PlayerStatisticService(BettingMatchRepository bettingMatchRepo, BettingPlayerRepository bettingPlayerRepo) {
        this.bettingMatchRepo = bettingMatchRepo;
        this.bettingPlayerRepo = bettingPlayerRepo;
    }

    public TotalPlayerStatistic playerStatistic(PlayerStatisticInfo playerStatisticInfo) {
        String username = SecurityUtil.getCurrentLogin();
        TotalPlayerStatistic totalPlayerStatistic = new TotalPlayerStatistic();
        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        double totalLossAmount = 0;
        List<BettingMatch> bettingMatchs = bettingMatchRepo.findByGroupIdAndUsername(playerStatisticInfo.getGroupId(),
                username);
        if (bettingMatchs.size() != 0) {
            for (BettingMatch bettingMatch : bettingMatchs) {
                Optional<BettingPlayer> bettingPlayer = bettingMatch.getBettingPlayers().stream()
                        .filter(bPlayer -> bPlayer.getPlayer().getUsername().equals(username)).findFirst();
                PlayerStatistic playerStatistic = new PlayerStatistic();
                Match match = bettingMatch.getMatch();
                playerStatistic.setCompetitor1Name(match.getCompetitor1().getName());
                playerStatistic.setCompetitor2Name(match.getCompetitor2().getName());
                playerStatistic.setExpiredBetTime(bettingMatch.getExpiredTime());
                playerStatistic.setCompetitor1Balance(bettingMatch.getBalance1().doubleValue());
                playerStatistic.setCompetitor2Balance(bettingMatch.getBalance2().doubleValue());
                if (match.getScore1() != null || match.getScore2() != null) {
                    playerStatistic.setCompetitor1Score(match.getScore1());
                    playerStatistic.setCompetitor2Score(match.getScore2());
                } else {
                    // doesn't have score here but Long - datatype auto set
                    // value = 0 if it null -> set it = -1 and UI will check it
                    // and show right data to user
                    playerStatistic.setCompetitor1Score(-1);
                    playerStatistic.setCompetitor2Score(-1);
                }
                // count lost amount when user bet
                if (bettingPlayer.isPresent()) {
                    Competitor betCompetitor = bettingPlayer.get().getBetCompetitor();
                    playerStatistic.setBetCompetitorName(betCompetitor.getName());
                    playerStatistic.setLossAmount(StatisticUtils.calculateLossAmount(bettingMatch, betCompetitor));
                }
                // count lost amount when user didnot bet
                else {
                    playerStatistic.setBetCompetitorName("--");
                    playerStatistic.setLossAmount(StatisticUtils.calculateLossAmount(bettingMatch, null));
                }
                playerStatistics.add(playerStatistic);
                totalLossAmount += playerStatistic.getLossAmount();
            }

        }
        totalPlayerStatistic.setPlayerStatistics(playerStatistics);
        totalPlayerStatistic.setTotalLossAmount(totalLossAmount);
        return totalPlayerStatistic;
    }

    public double getLostAmountByUser(Long bettingMatchId) {
        User player = SecurityUtil.getCurrentLoginUser();
        BettingMatch bettingMatch = bettingMatchRepo.findOne(bettingMatchId);
        if (bettingMatch == null) {
            throw new DataInvalidException("exception.existMatchEntity.message");
        } else if (bettingMatch.getMatch().getScore1() == null || bettingMatch.getMatch().getScore2() == null) {
            return 0;
        } else {
            BettingPlayer bettingPlayer = bettingPlayerRepo.findByPlayerAndBettingMatch(player, bettingMatch);
            if (bettingPlayer != null) {
                return StatisticUtils.calculateLossAmount(bettingMatch, bettingPlayer.getBetCompetitor());
            }
            return 0;
        }
    }
}
