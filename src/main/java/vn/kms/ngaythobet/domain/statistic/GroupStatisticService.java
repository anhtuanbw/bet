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
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.core.User.Role;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.SecurityUtil;

/**
 * 
 * @author tungvo
 *
 */

@Service
public class GroupStatisticService {
    private static final Logger logger = LoggerFactory.getLogger(GroupStatisticService.class);

    private final GroupRepository groupRepo;
    private final BettingMatchRepository bettingMatchRepo;
    private final UserRepository userRepo;

    @Autowired
    public GroupStatisticService(GroupRepository groupRepo, BettingMatchRepository bettingMatchRepo,
            UserRepository userRepo) {
        this.groupRepo = groupRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public List<GroupStatistic> getGroupStatistic(Long groupId) {
        List<GroupStatistic> groupStatistics = new ArrayList<GroupStatistic>();
        Group group = groupRepo.findOne(groupId);
        if (group == null) {
            throw new DataInvalidException("exception.group.not-exist");
        }

        User currentUser = SecurityUtil.getCurrentLoginUser();

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            Optional<User> optionalUser = group.getMembers().stream()
                    .filter(user -> user.getUsername().equals(currentUser.getUsername())).findFirst();
            if (!optionalUser.isPresent()) {
                throw new DataInvalidException("exception.group.not-belong-group");
            }

        }
        List<User> users = group.getMembers();
        for (User user : users) {
            GroupStatistic groupStatistic = new GroupStatistic();
            groupStatistic.setPlayer(user.getName());

            List<BettingMatch> bettingMatches = bettingMatchRepo.findByGroupIdAndUsername(groupId, user.getUsername());
            int notBetCount = 0;
            double notBetAmount = 0;
            int lossCount = 0;
            double lossAmount = 0;
            int notLossCount = 0;

            for (BettingMatch bettingMatch : bettingMatches) {

                Optional<BettingPlayer> optionalBettingPlayer = bettingMatch.getBettingPlayers().stream()
                        .filter(bettingPlayer -> bettingPlayer.getPlayer().getId().equals(user.getId())).findFirst();

                if (optionalBettingPlayer.isPresent()) {
                    BettingPlayer bp = optionalBettingPlayer.get();

                    BettingMatch bm = bp.getBettingMatch();
                    Competitor betCompetitor = bp.getBetCompetitor();

                    double tempLossAmount = StatisticUtils.calculateLossAmount(bm, betCompetitor);

                    if (tempLossAmount > 0) {
                        lossCount++;
                        lossAmount += tempLossAmount;
                    } else {
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
            groupStatistic.setTotalLossAmount(groupStatistic.getTotalLossAmount());
            groupStatistics.add(groupStatistic);
        }
        return groupStatistics;
    }
}
