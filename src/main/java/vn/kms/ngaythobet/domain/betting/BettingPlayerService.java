package vn.kms.ngaythobet.domain.betting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.AddCommentInfo;
import vn.kms.ngaythobet.web.dto.BettingMatchStatisticsInfo;
import vn.kms.ngaythobet.web.dto.PlayerBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdatePlayerBettingMatchInfo;

@Service
@Transactional
public class BettingPlayerService {

    private final BettingMatchRepository bettingMatchRepo;
    private final BettingPlayerRepository bettingPlayerRepo;
    private final CompetitorRepository competitorRepo;
    private final UserRepository userRepo;

    @Autowired
    public BettingPlayerService(BettingMatchRepository bettingMatchRepo, BettingPlayerRepository bettingPlayerRepo,
            CompetitorRepository competitorRepo, UserRepository userRepo) {
        this.bettingMatchRepo = bettingMatchRepo;
        this.bettingPlayerRepo = bettingPlayerRepo;
        this.competitorRepo = competitorRepo;
        this.userRepo = userRepo;
    }

    public void playBet(PlayerBettingMatchInfo playerBettingMatchInfo) {
        BettingMatch bettingMatch = bettingMatchRepo.findOne(playerBettingMatchInfo.getBettingMatchId());
        String comment = playerBettingMatchInfo.getComment();
        if (isExpired(bettingMatch.getExpiredTime())) {
            throw new DataInvalidException("exception.bettingPlayer.service.bettingMatch-is-expired");
        } else if (!bettingMatch.isActivated()) {
            throw new DataInvalidException("exception.bettingPlayer.service.bettingMatch-not-active");
        } else if (!isValidCompetitor(playerBettingMatchInfo.getCompetitorId(), bettingMatch.getMatch())) {
            throw new DataInvalidException("exception.bettingPlayer.service.competitor-belong-match");
        } else if (isBet(bettingMatch)) {
            throw new DataInvalidException("exception.bettingPlayer.service.already-bet");
        } else {
            BettingPlayer bettingPlayer = new BettingPlayer();
            User player = userRepo.findOneByUsername(SecurityUtil.getCurrentLogin()).get();
            bettingPlayer.setPlayer(player);
            bettingPlayer.setBettingMatch(bettingMatch);
            Competitor betCompetitor = competitorRepo.findOne(playerBettingMatchInfo.getCompetitorId());
            bettingPlayer.setBetCompetitor(betCompetitor);
            bettingPlayerRepo.save(bettingPlayer);
            if (StringUtils.isNotBlank(comment.trim())) {
                bettingMatch.setComment(comment);
                bettingMatchRepo.save(bettingMatch);
            }
        }

    }

    public void addComment(AddCommentInfo addCommentInfo) {
        BettingMatch bettingMatch = bettingMatchRepo.findOne(addCommentInfo.getBettingMatchId());
        bettingMatch.setComment(addCommentInfo.getComment());
        bettingMatchRepo.save(bettingMatch);
    }

    public void updatePlayBet(UpdatePlayerBettingMatchInfo playerBettingMatchInfo) {
        BettingPlayer bettingPlayer = bettingPlayerRepo.findOne(playerBettingMatchInfo.getBettingPlayerId());
        if (!isAuthor(bettingPlayer.getPlayer().getUsername())) {
            throw new DataInvalidException("exception.unauthorized");
        } else if (!bettingPlayer.getBettingMatch().isActivated()) {
            throw new DataInvalidException("exception.bettingPlayer.service.bettingMatch-not-active");
        } else if (isExpired(bettingPlayer.getBettingMatch().getExpiredTime())) {
            throw new DataInvalidException("exception.bettingPlayer.service.bettingMatch-is-expired");
        } else if (!isValidCompetitor(playerBettingMatchInfo.getCompetitorId(),
                bettingPlayer.getBettingMatch().getMatch())) {
            throw new DataInvalidException("exception.bettingPlayer.service.competitor-belong-match");
        } else {
            Competitor betCompetitor = competitorRepo.findOne(playerBettingMatchInfo.getCompetitorId());
            bettingPlayer.setBetCompetitor(betCompetitor);
            bettingPlayerRepo.save(bettingPlayer);
        }
    }

    private boolean isAuthor(String username) {
        return SecurityUtil.getCurrentLogin().equals(username);
    }

    private boolean isExpired(LocalDateTime time) {
        return time.isBefore(LocalDateTime.now());
    }

    private boolean isValidCompetitor(Long competitorId, Match match) {
        return (competitorId.equals(match.getCompetitor1().getId())
                || competitorId.equals(match.getCompetitor2().getId()));
    }

    private boolean isBet(BettingMatch bettingMatch) {
        User player = userRepo.findOneByUsername(SecurityUtil.getCurrentLogin()).get();
        return (bettingPlayerRepo.findByPlayerAndBettingMatch(player, bettingMatch) != null);
    }

    public BettingMatchStatisticsInfo getBettingMatchStatistics(Long bettingMatchId) {
        BettingMatch bettingMatch = bettingMatchRepo.findByIdAndActivated(bettingMatchId, true).get();
        Match match = bettingMatch.getMatch();
        List<BettingPlayer> bettingPlayersChoosingTeam1 = bettingPlayerRepo
                .findByBettingMatchIdAndBetCompetitorId(bettingMatchId, match.getCompetitor1().getId());
        List<BettingPlayer> bettingPlayersChoosingTeam2 = bettingPlayerRepo
                .findByBettingMatchIdAndBetCompetitorId(bettingMatchId, match.getCompetitor2().getId());
        Group group = bettingMatch.getGroup();
        List<User> users = group.getMembers();
        List<BettingPlayer> bettingPlayers = new ArrayList<BettingPlayer>();
        bettingPlayers.addAll(bettingPlayersChoosingTeam1);
        bettingPlayers.addAll(bettingPlayersChoosingTeam2);
        BettingMatchStatisticsInfo info = new BettingMatchStatisticsInfo();
        info.setBettingPlayersChooseCompetitor1(bettingPlayersChoosingTeam1);
        info.setBettingPlayersChooseCompetitor2(bettingPlayersChoosingTeam2);
        int percentOfChoosingCompetitor1 = bettingPlayersChoosingTeam1.size() * 100 / users.size();
        int percentOfChoosingCompetitor2 = bettingPlayersChoosingTeam2.size() * 100 / users.size();
        info.setPercentOfChoosingCompetitor1(percentOfChoosingCompetitor1);
        info.setPercentOfChoosingCompetitor2(percentOfChoosingCompetitor2);
        info.setTotalBettingPlayers(users);
        List<User> userNotBet = getUsersNotBet(users, bettingPlayers);
        info.setUserNotBet(userNotBet);
        return info;
    }

    private List<User> getUsersNotBet(List<User> users, List<BettingPlayer> bettingPlayers) {
        List<User> userNotBet = new ArrayList<User>();
        for (User user : users) {
            boolean isExist = false;
            for (BettingPlayer bettingPlayer : bettingPlayers) {
                if (bettingPlayer.getPlayer().getId() == user.getId()) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                userNotBet.add(user);
            }
        }
        return userNotBet;
    }

    public BettingPlayer getBettingPlayerOfCurrentUserByBettingMatchId(Long bettingMatchId) {
        User user = userRepo.findOneByUsername(SecurityUtil.getCurrentLogin()).get();
        BettingMatch bettingMatch = bettingMatchRepo.findOne(bettingMatchId);
        BettingPlayer bettingPlayer = bettingPlayerRepo.findByPlayerAndBettingMatch(user, bettingMatch);
        return bettingPlayer;
    }
}
