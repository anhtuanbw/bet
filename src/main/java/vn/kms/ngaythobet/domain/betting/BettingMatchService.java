package vn.kms.ngaythobet.domain.betting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.tournament.MatchRepository;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.tournament.RoundRepository;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.ActiveBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.CreateBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdateBettingMatchInfo;

@Service
@Transactional
public class BettingMatchService {

    private final MatchRepository matchRepo;
    private final GroupRepository groupRepo;
    private final BettingMatchRepository bettingMatchRepo;
    private final RoundRepository roundRepo;

    @Autowired
    public BettingMatchService(MatchRepository matchRepo, GroupRepository groupRepo,
            BettingMatchRepository bettingMatchRepo, RoundRepository roundRepo) {
        this.matchRepo = matchRepo;
        this.groupRepo = groupRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.roundRepo = roundRepo;
    }

    public boolean bettingMatchIsExisted(Long groupId, Long matchId) {
        List<BettingMatch> bettingMatchs = new ArrayList<>();
        bettingMatchs = bettingMatchRepo.findAllByOrderByCreatedAtDesc();
        for (BettingMatch bettingMatch : bettingMatchs) {
            if (bettingMatch.getGroup().getId() == groupId && bettingMatch.getMatch().getId() == matchId) {
                return false;
            }
        }
        return true;
    }

    private boolean balanceIsValid(BigDecimal balance) {
        return (balance.doubleValue() % 0.25 == 0);
    }

    public void createBettingMatch(CreateBettingMatchInfo createBettingMatchInfo) {
        BettingMatch bettingMatch = new BettingMatch();
        if (bettingMatchIsExisted(createBettingMatchInfo.getGroupId(), createBettingMatchInfo.getMatchId())) {
            if (balanceIsValid(createBettingMatchInfo.getBalance1())
                    && balanceIsValid(createBettingMatchInfo.getBalance2())) {
                bettingMatch.setBalance1(createBettingMatchInfo.getBalance1());
                bettingMatch.setBalance2(createBettingMatchInfo.getBalance2());
                bettingMatch.setExpiredTime(createBettingMatchInfo.getExpiredTime());
                bettingMatch.setBetAmount(createBettingMatchInfo.getBetAmount());
                bettingMatch.setMatch(matchRepo.getOne(createBettingMatchInfo.getMatchId()));
                bettingMatch.setGroup(groupRepo.getOne(createBettingMatchInfo.getGroupId()));
                bettingMatch.setDescription(createBettingMatchInfo.getDecription());
                bettingMatch.setActivated(createBettingMatchInfo.isActivated());
                bettingMatchRepo.save(bettingMatch);
            } else {
                throw new DataInvalidException("exception.balance.in.valid");
            }
        } else
            throw new DataInvalidException("exception.bettingMatch.is.existed");
    }

    public void updateBettingMatch(UpdateBettingMatchInfo updateBettingMatchInfo) {
        BettingMatch bettingMatch = bettingMatchRepo.findOne(updateBettingMatchInfo.getBettingMatchId());
        if (updateBettingMatchInfo.getExpiredTime().isAfter(bettingMatch.getExpiredTime())) {
            if (balanceIsValid(updateBettingMatchInfo.getBalance1())
                    && balanceIsValid(updateBettingMatchInfo.getBalance2())) {
                bettingMatch.setBalance1(updateBettingMatchInfo.getBalance1());
                bettingMatch.setBalance2(updateBettingMatchInfo.getBalance2());
                bettingMatch.setBetAmount(updateBettingMatchInfo.getBetAmount());
                bettingMatch.setMatch(matchRepo.getOne(updateBettingMatchInfo.getMatchId()));
                bettingMatch.setDescription(updateBettingMatchInfo.getDecription());
                bettingMatch.setActivated(updateBettingMatchInfo.isActivated());
                bettingMatchRepo.save(bettingMatch);
            } else {
                throw new DataInvalidException("exception.balance.in.valid");
            }
        } else {
            throw new DataInvalidException("exception.update.time.in.valid");
        }
    }

    public void activeBettingMatch(ActiveBettingMatchInfo activeBettingMatchInfo) {
        BettingMatch bettingMatch = bettingMatchRepo.findOne(activeBettingMatchInfo.getBettingMatchId());
        bettingMatch.setActivated(true);
        bettingMatchRepo.save(bettingMatch);
    }

    public List<BettingMatch> getBettingMatchesByMatch(Long matchId) {
        Match match = matchRepo.getOne(matchId);
        return bettingMatchRepo.findByMatch(match);
    }

    public List<BettingMatch> getBettingMatchesByRoundAndGroupId(long roundId, long groupId) {
        List<BettingMatch> bettingMatches = new ArrayList<>();
        Round round = roundRepo.getOne(roundId);
        Group group = groupRepo.getOne(groupId);
        if (round != null && group != null) {
            List<Match> matches = matchRepo.findByRound(round);
            for (Match match : matches) {
                bettingMatches.add(bettingMatchRepo.findByGroupAndMatch(group, match));
            }
        } else {
            throw new DataInvalidException("exception.round.group.in.valid");
        }
        return bettingMatches;
    }

    public BettingMatch getBettingMatchById(Long id) {
        return bettingMatchRepo.findOne(id);
    }

    @Transactional(readOnly = true)
    public BettingMatch findActiveBettingMatchById(Long id) {
        return bettingMatchRepo.findByIdAndActivated(id, true).get();
    }
}
