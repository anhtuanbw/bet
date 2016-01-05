package vn.kms.ngaythobet.domain.statistic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.GetPlayerBettingMatchesByPlayerAndGroupInfo;

@Service
@Transactional
public class PlayerStatisticService {

    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final BettingMatchRepository bettingMatchRepo;
    private final BettingPlayerRepository bettingPlayerRepo;

    @Autowired
    public PlayerStatisticService(UserRepository userRepo, GroupRepository groupRepo,
            BettingMatchRepository bettingMatchRepo, BettingPlayerRepository bettingPlayerRepo) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.bettingPlayerRepo = bettingPlayerRepo;
    }

    public List<PlayerStatistic> getPlayerBettingMatchesByPlayerAndGroup(
            GetPlayerBettingMatchesByPlayerAndGroupInfo getPlayerBettingMatchesByPlayerAndGroupInfo) {
        String username = SecurityUtil.getCurrentLogin();
        User player = userRepo.findOneByUsername(username).get();
        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        List<BettingMatch> bettingMatches = bettingMatchRepo
                .findByGroup(groupRepo.getOne(getPlayerBettingMatchesByPlayerAndGroupInfo.getGroupId()));
        List<BettingMatch> bettingMatchesPlayerDidnotJoin = new ArrayList<>();
        List<BettingPlayer> bettingPlayers = new ArrayList<>();
        for (BettingMatch bettingMatch : bettingMatches) {
            BettingPlayer bettingPlayer = bettingPlayerRepo.findByPlayerAndBettingMatch(player, bettingMatch);
            if (bettingPlayer != null) {
                bettingPlayers.add(bettingPlayer);
            } else {
                bettingMatchesPlayerDidnotJoin.add(bettingMatch);
            }
        }
        // count lost amount when user bet
        for (BettingPlayer bettingPlayer : bettingPlayers) {
            PlayerStatistic playerStatistic = new PlayerStatistic();
            BettingMatch bettingMatch = bettingPlayer.getBettingMatch();
            Match match = bettingMatch.getMatch();
            playerStatistic.setPlayer(username);
            playerStatistic.setCompetitor1Name(match.getCompetitor1().getName());
            playerStatistic.setCompetitor2Name(match.getCompetitor2().getName());
            playerStatistic.setExpiredBetTime(bettingMatch.getExpiredTime());
            playerStatistic.setCompetitor1Score(match.getScore1());
            playerStatistic.setCompetitor2Score(match.getScore2());
            playerStatistic.setCompetitor1Balance(bettingMatch.getBalance1().doubleValue());
            playerStatistic.setCompetitor2Balance(bettingMatch.getBalance2().doubleValue());
            playerStatistic.setBetCompetitorName(bettingPlayer.getBetCompetitor().getName());
            playerStatistic.setLossAmount(CaculateLostAmount(match, bettingMatch, bettingPlayer.getBetCompetitor()));
            playerStatistics.add(playerStatistic);
        }
        // count lost amout when user didnot bet
        for (BettingMatch bettingMatch : bettingMatchesPlayerDidnotJoin) {
            PlayerStatistic playerStatistic = new PlayerStatistic();
            Match match = bettingMatch.getMatch();
            playerStatistic.setCompetitor1Name(match.getCompetitor1().getName());
            playerStatistic.setCompetitor2Name(match.getCompetitor2().getName());
            playerStatistic.setPlayer(username);
            playerStatistic.setExpiredBetTime(bettingMatch.getExpiredTime());
            playerStatistic.setCompetitor1Score(match.getScore1());
            playerStatistic.setCompetitor2Score(match.getScore2());
            playerStatistic.setCompetitor1Balance(bettingMatch.getBalance1().doubleValue());
            playerStatistic.setCompetitor2Balance(bettingMatch.getBalance2().doubleValue());
            playerStatistic.setBetCompetitorName("--");
            playerStatistic.setLossAmount(bettingMatch.getBetAmount().doubleValue());
            playerStatistics.add(playerStatistic);
        }

        return playerStatistics;
    }

    private double CaculateLostAmount(Match match, BettingMatch bettingMatch, Competitor competitor) {
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
