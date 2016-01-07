package vn.kms.ngaythobet.domain.statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.util.DataInvalidException;

/**
 * 
 * @author tungvo
 *
 */

@Service
public class GroupStatisticService {
    private static final Logger logger = LoggerFactory
            .getLogger(GroupStatisticService.class);

    private final GroupRepository groupRepo;
    private final BettingMatchRepository bettingMatchRepo;
    private final UserService userService;

    @Autowired
    public GroupStatisticService(GroupRepository groupRepo,
            BettingMatchRepository bettingMatchRepo, UserService userService) {
        this.groupRepo = groupRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.userService = userService;
    }

    @Transactional
    public List<GroupStatistic> getGroupStatistic(Long groupId) {
        List<GroupStatistic> groupStatistics = new ArrayList<GroupStatistic>();

        Group group = groupRepo.findOne(groupId);
        if (group == null) {
            throw new DataInvalidException("exception.group.not-exist");
        }

        User currentUser = userService.getUserInfo();

        Group groupOfcurrentUser = groupRepo.findByIdAndMembers(groupId,
                currentUser);

        if (groupOfcurrentUser == null) {
            throw new DataInvalidException("exception.group.not-belong-group");
        }

        List<User> users = group.getMembers();
        for (User user : users) {
            GroupStatistic groupStatistic = new GroupStatistic();
            groupStatistic.setPlayer(user.getName());

            List<BettingMatch> bettingMatches = bettingMatchRepo
                    .findByGroupIdAndUsername(groupId, user.getUsername());
            int notBetCount = 0;
            long notBetAmount = 0;
            int lossCount = 0;
            long lossAmount = 0;
            int notLossCount = 0;

            for (BettingMatch bettingMatch : bettingMatches) {

                Optional<BettingPlayer> optionalBettingPlayer = bettingMatch
                        .getBettingPlayers()
                        .stream()
                        .filter(bettingPlayer -> bettingPlayer.getPlayer()
                                .getId().equals(user.getId())).findFirst();


                if (optionalBettingPlayer.isPresent()) {
                    BettingPlayer bp = optionalBettingPlayer.get();

                    BettingMatch bm = bp.getBettingMatch();
                    Competitor betCompetitor = bp.getBetCompetitor();
                    Match match = bm.getMatch();

                    if (match.getScore1() == null || match.getScore2() == null) {
                        continue;
                    }

                    Long competitor1Score = match.getScore1();
                    Long competitor2Score = match.getScore2();

                    Long balance1 = bm.getBalance1().longValue();
                    Long balance2 = bm.getBalance2().longValue();

                    Long totalScore1 = competitor1Score + balance1;
                    Long totalScore2 = competitor2Score + balance2;

                    double diff = 0;

                    if (betCompetitor.getId().equals(
                            match.getCompetitor1().getId())) {
                        diff = totalScore1 - totalScore2;
                    } else {
                        diff = totalScore2 - totalScore1;
                    }

                    if (diff > -0.5 && diff < 0.25) {
                        lossCount++;
                        lossAmount += bm.getBetAmount().longValue() / 2;
                    }

                    if (diff <= -0.5) {
                        lossCount++;
                        lossAmount += bm.getBetAmount().longValue();
                    }

                    if (diff >= 0.25) {
                        notLossCount++;
                    }

                } else {
                    notBetCount++;
                    notBetAmount += bettingMatch.getBetAmount().longValue();
                }
            }
            groupStatistic.setNotbetCount(notBetCount);
            groupStatistic.setNotbetAmount(notBetAmount);
            groupStatistic.setLossCount(lossCount);
            groupStatistic.setLossAmount(lossAmount);
            groupStatistic.setNotlossCount(notLossCount);
            groupStatistic.setTotalLossAmount(groupStatistic
                    .getTotalLossAmount());
            groupStatistics.add(groupStatistic);
        }
        return groupStatistics;
    }
}
